package com.autoscolombia.parqueadero.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.autoscolombia.parqueadero.model.Registro;
import com.autoscolombia.parqueadero.model.Usuario;
import com.autoscolombia.parqueadero.model.Vehiculo;
import com.autoscolombia.parqueadero.repository.RegistroRepository;

@Service
public class RegistroService {

    private final RegistroRepository registroRepository;

    public RegistroService(RegistroRepository registroRepository) {
        this.registroRepository = registroRepository;
    }

    public List<Registro> listar() {
        return registroRepository.findAll();
    }

    public List<Registro> listarActivos() {
        return registroRepository.findByEstado("ABIERTA");
    }

    public void registrarEntrada(String placa, String tipoVehiculo, Usuario usuario) {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca(placa);
        vehiculo.setTipo(tipoVehiculo);

        Registro registro = new Registro();
        registro.setVehiculo(vehiculo);
        registro.setFechaIngreso(LocalDateTime.now());
        registro.setEstado("ABIERTA");
        registro.setUsuario(usuario); // Guardamos el usuario real que hace el registro

        registroRepository.save(registro);
    }

    public void registrarSalida(Long id) {
        Registro registro = registroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));

        registro.setFechaSalida(LocalDateTime.now());
        registro.setEstado("CERRADA");

        registroRepository.save(registro);
    }
}
