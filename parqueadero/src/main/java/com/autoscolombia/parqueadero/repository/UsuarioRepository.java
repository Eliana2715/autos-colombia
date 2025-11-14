package com.autoscolombia.parqueadero.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autoscolombia.parqueadero.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findByTipo(String tipo);
}
