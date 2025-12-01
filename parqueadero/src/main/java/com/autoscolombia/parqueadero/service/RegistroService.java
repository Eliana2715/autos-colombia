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

    public RegistroService(RegistroRepository registroRepository, CeldaService celdaService,
                            UsuarioService usuarioService, VehiculoRepository vehiculoRepository) {
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
    public void guardar(Registro registro, Long celdaId, Long usuarioId) {

        Celda celda = celdaService.buscarPorId(celdaId);
        if (celda == null) {
            throw new RuntimeException("Celda no encontrada");
        }

        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        // Vehículo tomado del formulario
        String placa = registro.getVehiculo().getPlaca();
        String tipoVehiculo = registro.getVehiculo().getTipo();

        Vehiculo vehiculo = vehiculoRepository.findByPlaca(placa).orElse(null);

        if (vehiculo == null) {
            vehiculo = new Vehiculo();
            vehiculo.setPlaca(placa);
            vehiculo.setTipo(tipoVehiculo);
            vehiculo = vehiculoRepository.save(vehiculo);
        }

        registro.setVehiculo(vehiculo);
        registro.setUsuario(usuario);
        registro.setCelda(celda);
        registro.setFechaIngreso(LocalDateTime.now());
        registro.setEstado("ABIERTA");

        registroRepository.save(registro);

        celdaService.marcarOcupada(celda.getCeldaId());
    }


    @Transactional
    public void registrarEntrada(String placa, String tipoVehiculo, Long celdaId, Usuario usuario) {

        Celda celda = celdaService.buscarPorId(celdaId);
        if (celda == null) {
            throw new RuntimeException("Celda no encontrada con id: " + celdaId);
        }

        Vehiculo vehiculo = vehiculoRepository.findByPlaca(placa).orElse(null);
        if (vehiculo == null) {
            vehiculo = new Vehiculo();
            vehiculo.setPlaca(placa);
            vehiculo.setTipo(tipoVehiculo);
            vehiculo = vehiculoRepository.save(vehiculo);
        }

        Registro registro = new Registro();
        registro.setVehiculo(vehiculo);
        registro.setFechaIngreso(LocalDateTime.now());
        registro.setEstado("ABIERTA");
        registro.setUsuario(usuario);
        registro.setCelda(celda);

        registroRepository.save(registro);

        celdaService.marcarOcupada(celda.getCeldaId());
    }

    
    @Transactional
    public void registrarSalida(Long registroId) {
        Registro registro = registroRepository.findById(registroId)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));

        registro.setFechaSalida(LocalDateTime.now());
        registro.setEstado("CERRADA");

        calcularTiempoYValor(registro);

        registroRepository.save(registro);

        celdaService.marcarDisponible(registro.getCelda().getCeldaId());
    }

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

    
    public Registro buscarPorId(Long id) {
        return registroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));
    }

    public void eliminar(Long id) {
        Registro registro = buscarPorId(id);
        registroRepository.delete(registro);
        celdaService.marcarDisponible(registro.getCelda().getCeldaId());
    }

    
    @Transactional
    public void actualizar(Long registroId, String placa, String tipoVehiculo, Long celdaId, Long usuarioId) {

        Registro registro = buscarPorId(registroId);

        // Vehículo
        Vehiculo vehiculo = registro.getVehiculo();
        vehiculo.setPlaca(placa);
        vehiculo.setTipo(tipoVehiculo);
        vehiculoRepository.save(vehiculo);

        // Celda
        Celda celda = celdaService.buscarPorId(celdaId);
        registro.setCelda(celda);

        // Usuario
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        registro.setUsuario(usuario);

        registroRepository.save(registro);
    }
}
