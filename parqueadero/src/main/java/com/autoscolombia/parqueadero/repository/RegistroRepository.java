package com.autoscolombia.parqueadero.repository;

import com.autoscolombia.parqueadero.model.Registro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RegistroRepository extends JpaRepository<Registro, Long> {
    List<Registro> findByEstado(String estado);
}
