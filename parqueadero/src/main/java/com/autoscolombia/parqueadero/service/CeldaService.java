package com.autoscolombia.parqueadero.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.autoscolombia.parqueadero.model.Celda;
import com.autoscolombia.parqueadero.repository.CeldaRepository;

@Service
public class CeldaService {

    private final CeldaRepository celdaRepository;

    public CeldaService(CeldaRepository celdaRepository) {
        this.celdaRepository = celdaRepository;
    }

    public List<Celda> listar() {
        return celdaRepository.findAll();
    }

    public Celda guardar(Celda celda) {
        return celdaRepository.save(celda);
    }

    public Celda buscarPorId(Long id) {
        return celdaRepository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        celdaRepository.deleteById(id);
    }

    public List<Celda> listarDisponibles() {
        return celdaRepository.findByEstado("libre");
    }

}
