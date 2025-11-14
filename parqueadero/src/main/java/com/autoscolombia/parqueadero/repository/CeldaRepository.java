package com.autoscolombia.parqueadero.repository;


import com.autoscolombia.parqueadero.model.Celda;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CeldaRepository extends JpaRepository<Celda, Long> {
    Optional<Celda> findByCodigo(String codigo);
}