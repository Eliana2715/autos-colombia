package com.autoscolombia.parqueadero.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autoscolombia.parqueadero.model.Celda;
import com.autoscolombia.parqueadero.model.Registro;
import com.autoscolombia.parqueadero.model.Usuario;
import com.autoscolombia.parqueadero.model.Vehiculo;
import com.autoscolombia.parqueadero.repository.CeldaRepository;
import com.autoscolombia.parqueadero.repository.RegistroRepository;
import com.autoscolombia.parqueadero.repository.UsuarioRepository;
import com.autoscolombia.parqueadero.repository.VehiculoRepository;

@Service
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final RegistroRepository registroRepository;
    private final CeldaRepository celdaRepository;
    private final UsuarioRepository usuarioRepository;

    public VehiculoService(VehiculoRepository vehiculoRepository,
                           RegistroRepository registroRepository,
                           CeldaRepository celdaRepository,
                           UsuarioRepository usuarioRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.registroRepository = registroRepository;
        this.celdaRepository = celdaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // ======================
    // CRUD Vehículo
    // ======================
    public List<Vehiculo> listarTodos() {
        return vehiculoRepository.findAll();
    }

    public Vehiculo buscarPorId(Long id) {
        return vehiculoRepository.findById(id).orElse(null);
    }

    public Vehiculo buscarPorPlaca(String placa) {
        return vehiculoRepository.findByPlaca(placa).orElse(null);
    }

    public Vehiculo guardarVehiculo(Vehiculo vehiculo) {
        return vehiculoRepository.save(vehiculo);
    }

    public void eliminarVehiculo(Long id) {
        vehiculoRepository.deleteById(id);
    }

    // ======================
    // Funciones del flujo
    // ======================

    // 1️⃣ Vehículos con registro activo
    public List<Registro> listarVehiculosActivos() {
        return registroRepository.findByEstado("ABIERTA");
    }

    // 2️⃣ Listar celdas libres
    public List<Celda> listarCeldasLibres() {
        return celdaRepository.findAll().stream()
                .filter(c -> c.getEstado().equalsIgnoreCase("libre"))
                .toList();
    }

    // 3️⃣ Listar usuarios
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // 4️⃣ Registrar entrada al parqueadero
    @Transactional
    public void registrarEntrada(Vehiculo vehiculo, Long usuarioId, Long celdaId) {
        if (vehiculo.getVehiculoId() == null) {
            vehiculoRepository.save(vehiculo);
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));
        Celda celda = celdaRepository.findById(celdaId)
                .orElseThrow(() -> new RuntimeException("Celda no encontrada con ID: " + celdaId));

        Registro registro = new Registro();
        registro.setVehiculo(vehiculo);
        registro.setUsuario(usuario);
        registro.setCelda(celda);
        registro.setFechaIngreso(LocalDateTime.now());
        registro.setEstado("ABIERTA");

        registroRepository.save(registro);

        celda.setEstado("ocupado");
        celdaRepository.save(celda);

        System.out.println("Entrada registrada correctamente para vehículo: " + vehiculo.getPlaca());
    }

    // 5️⃣ Buscar registro activo por vehículo
    public Registro buscarRegistroActivoPorVehiculo(Long vehiculoId) {
        return registroRepository.findByEstado("ABIERTA").stream()
                .filter(r -> r.getVehiculo().getVehiculoId().equals(vehiculoId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay registro activo para este vehículo"));
    }

}
