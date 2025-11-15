package com.autoscolombia.parqueadero.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.autoscolombia.parqueadero.model.Registro;
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

    public void registrarEntrada(String placa, String tipoVehiculo, String celdaCodigo, Long usuarioId) {

    }

    public void registrarSalida(Long id) {
    
    }
    
    public List<Registro> listarActivos() {
        return registroRepository.findByEstado("ABIERTA");
    }
}
