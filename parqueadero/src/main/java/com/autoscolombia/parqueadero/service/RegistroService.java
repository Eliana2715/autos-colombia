package com.autoscolombia.parqueadero.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autoscolombia.parqueadero.model.Celda;
import com.autoscolombia.parqueadero.model.Registro;
import com.autoscolombia.parqueadero.model.Usuario;
import com.autoscolombia.parqueadero.model.Vehiculo;
import com.autoscolombia.parqueadero.repository.RegistroRepository;
import com.autoscolombia.parqueadero.repository.VehiculoRepository;

@Service
public class RegistroService {

    private final RegistroRepository registroRepository;
    private final CeldaService celdaService;
    private final UsuarioService usuarioService;
    private final VehiculoRepository vehiculoRepository;

    public RegistroService(RegistroRepository registroRepository, CeldaService celdaService, UsuarioService usuarioService, VehiculoRepository vehiculoRepository) {
        this.registroRepository = registroRepository;
        this.celdaService = celdaService;
        this.usuarioService = usuarioService;
        this.vehiculoRepository = vehiculoRepository;
    }

    public List<Registro> listar() {
        return registroRepository.findAll();
    }

    public List<Registro> listarActivos() {
        return registroRepository.findByEstado("ABIERTA");
    }

@Transactional
public void registrarEntrada(String placa, String tipoVehiculo, Long celdaId, Usuario usuario) {

    // Validar celda
    if (celdaId == null) {
        throw new IllegalArgumentException("Debe seleccionar una celda válida");
    }

    Celda celda = celdaService.buscarPorId(celdaId);
    if (celda == null) {
        throw new RuntimeException("Celda no encontrada con id: " + celdaId);
    }

    // 🔹 Buscar vehículo existente por placa
    Vehiculo vehiculo = vehiculoRepository.findByPlaca(placa).orElse(null);
    if (vehiculo == null) {
        vehiculo = new Vehiculo();
        vehiculo.setPlaca(placa);
        vehiculo.setTipo(tipoVehiculo);
        vehiculo = vehiculoRepository.save(vehiculo); // 🔹 Guardamos antes de asociar
    }

    // 🔹 Crear registro
    Registro registro = new Registro();
    registro.setVehiculo(vehiculo);
    registro.setFechaIngreso(LocalDateTime.now());
    registro.setEstado("ABIERTA");
    registro.setUsuario(usuario);
    registro.setCelda(celda);

    registroRepository.save(registro);

    // 🔹 Marcar celda como ocupada
    celda.setEstado("ocupado");
    celdaService.guardar(celda); // Método que haga save en CeldaRepository
}


    // ============================
    // Registrar salida del parqueadero
    // ============================
    @Transactional
    public void registrarSalida(Long registroId) {
        Registro registro = registroRepository.findById(registroId)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));

        registro.setFechaSalida(LocalDateTime.now());
        registro.setEstado("CERRADA");

        // 🔹 NUEVO: Calcular tiempo y valor a pagar
        calcularTiempoYValor(registro);

        registroRepository.save(registro);

        // Liberar celda
        if (registro.getCelda() != null) {
            celdaService.marcarDisponible(registro.getCelda().getCeldaId());
        }
    }

    // 🔹 MÉTODO MOVIDO DESDE VehiculoService
    private void calcularTiempoYValor(Registro registro) {
        if (registro.getFechaIngreso() != null && registro.getFechaSalida() != null) {
            Duration duracion = Duration.between(registro.getFechaIngreso(), registro.getFechaSalida());
            long minutos = duracion.toMinutes();
            long horas = minutos / 60;
            long mins = minutos % 60;

            registro.setTiempoTotal(horas + "h " + mins + "m");
            double valor = Math.ceil(minutos / 60.0) * 5000;
            registro.setValorPagar(valor);
        }
    }

    // ===============================
    // Métodos de utilidad para CRUD
    // ===============================
    public Registro buscarPorId(Long id) {
        return registroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));
    }

    public void eliminar(Long id) {
        Registro registro = buscarPorId(id);
        registroRepository.delete(registro);
        if (registro.getCelda() != null) {
            celdaService.marcarDisponible(registro.getCelda().getCeldaId());
        }
    }

    public void actualizar(Long registroId, String placa, String tipoVehiculo, Long celdaId, Long usuarioId) {
        Registro registro = buscarPorId(registroId);

        registro.getVehiculo().setPlaca(placa);
        registro.getVehiculo().setTipo(tipoVehiculo);

        Celda celda = celdaService.buscarPorId(celdaId);
        registro.setCelda(celda);

        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        registro.setUsuario(usuario);

        registroRepository.save(registro);
    }
}
