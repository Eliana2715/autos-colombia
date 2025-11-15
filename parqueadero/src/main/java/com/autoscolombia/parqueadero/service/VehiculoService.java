package com.autoscolombia.parqueadero.service;

import com.autoscolombia.parqueadero.model.*;
import com.autoscolombia.parqueadero.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

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

    // CRUD básico
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

    // ===========================
    // Funciones para mockups
    // ===========================

    // 1️⃣ Registrar entrada
    @Transactional
    public void registrarEntrada(Vehiculo vehiculo, Long usuarioId, Long celdaId) {
        if (vehiculo.getVehiculoId() == null) {
            vehiculoRepository.save(vehiculo);
        }

        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow();
        Celda celda = celdaRepository.findById(celdaId).orElseThrow();

        Registro registro = new Registro();
        registro.setVehiculo(vehiculo);
        registro.setUsuario(usuario);
        registro.setCelda(celda);
        registro.setFechaIngreso(LocalDateTime.now());
        registro.setEstado("ABIERTA");

        registroRepository.save(registro);

        celda.setEstado("ocupado");
        celdaRepository.save(celda);
    }

    // 2️⃣ Listar vehículos activos
    public List<Registro> listarVehiculosActivos() {
        return registroRepository.findByEstado("ABIERTA");
    }

    // 3️⃣ Registrar salida
    @Transactional
    public void registrarSalida(Long registroId) {
        Registro registro = registroRepository.findById(registroId).orElseThrow();
        registro.setFechaSalida(LocalDateTime.now());
        registro.setEstado("CERRADA");

        calcularTiempoYValor(registro);

        registroRepository.save(registro);

        Celda celda = registro.getCelda();
        celda.setEstado("libre");
        celdaRepository.save(celda);
    }

    // 4️⃣ Calcular tiempo y valor
    public void calcularTiempoYValor(Registro registro) {
        if (registro.getFechaIngreso() != null && registro.getFechaSalida() != null) {
            Duration duracion = Duration.between(registro.getFechaIngreso(), registro.getFechaSalida());
            long minutos = duracion.toMinutes();
            long horas = minutos / 60;
            long mins = minutos % 60;
            registro.setTiempoTotal(horas + "h " + mins + "m");

            registro.setValorPagar(Math.ceil(minutos / 60.0) * 5000);
        }
    }
}
