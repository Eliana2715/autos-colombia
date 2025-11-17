package com.autoscolombia.parqueadero.repository;

import com.autoscolombia.parqueadero.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    // Buscar vehículo por placa
    Optional<Vehiculo> findByPlaca(String placa);

    // Verificar existencia por placa
    boolean existsByPlaca(String placa);
}
