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
        if (id == null) {
            throw new IllegalArgumentException("Debe seleccionar una celda válida");
        }
        return celdaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Celda no encontrada con id: " + id));
    }

    public void eliminar(Long id) {
        celdaRepository.deleteById(id);
    }

    public List<Celda> listarDisponibles() {
        return celdaRepository.findByEstado("LIBRE");
    }


    public void marcarOcupada(Long celdaId) {
        Celda celda = buscarPorId(celdaId);
        celda.setEstado("OCUPADA");
        guardar(celda);
    }

    public void marcarDisponible(Long celdaId) {
    Celda celda = buscarPorId(celdaId);
        celda.setEstado("LIBRE");
        guardar(celda);
    }

    
}

