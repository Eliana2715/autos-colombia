package com.autoscolombia.parqueadero.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.autoscolombia.parqueadero.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByUsernameAndPassword(String username, String password);
}
