package com.autoscolombia.parqueadero.service;

import org.springframework.stereotype.Service;
import com.autoscolombia.parqueadero.model.Usuario;
import com.autoscolombia.parqueadero.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario validarLogin(String username, String password) {
        return usuarioRepository.findByUsernameAndPassword(username, password);
    }
}