package com.autoscolombia.parqueadero.service;

import com.autoscolombia.parqueadero.model.Usuario;
import com.autoscolombia.parqueadero.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }
    // Método para login
    public Usuario validarLogin(String username, String password) {
        return usuarioRepository.findByUsernameAndPassword(username, password);
    }
}
