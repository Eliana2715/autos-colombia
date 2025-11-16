package com.autoscolombia.parqueadero.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autoscolombia.parqueadero.model.Celda;

public interface CeldaRepository extends JpaRepository<Celda, Long> {

    Optional<Celda> findByCodigo(String codigo);

    // Buscar solo celdas libres
    List<Celda> findByEstado(String estado);
}