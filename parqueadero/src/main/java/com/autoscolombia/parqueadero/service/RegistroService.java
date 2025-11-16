package com.autoscolombia.parqueadero.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import com.autoscolombia.parqueadero.model.Celda;
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

    public Registro guardar(Registro registro) {
        return registroRepository.save(registro);
    }

    public Registro buscarPorId(Long id) {
        return registroRepository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        registroRepository.deleteById(id);
    }

    // Registrar entrada
    public void registroEntrada(String placa, String tipoVehiculo, Celda celda, Usuario usuario) {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca(placa);
        vehiculo.setTipo(tipoVehiculo);
        vehiculo.setCelda(celda); // ✅ Celda como objeto

        Registro registro = new Registro();
        registro.setVehiculo(vehiculo);
        registro.setFechaIngreso(LocalDateTime.now());
        registro.setEstado("ABIERTA");
        registro.setUsuario(usuario);

        registroRepository.save(registro);
    }

    // Registrar salida
    public void registrarSalida(Long id) {
        Registro registro = registroRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Registro no encontrado"));

        registro.setFechaSalida(LocalDateTime.now());
        registro.setEstado("CERRADA");

        registroRepository.save(registro);
    }

    public List<Registro> listarActivos() {
        return registroRepository.findByEstado("ABIERTA");
    }
}
