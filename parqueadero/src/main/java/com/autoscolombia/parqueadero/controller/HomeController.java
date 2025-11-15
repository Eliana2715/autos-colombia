package com.autoscolombia.parqueadero.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.autoscolombia.parqueadero.service.UsuarioService;
import com.autoscolombia.parqueadero.model.Usuario;

@Controller
public class HomeController {

    private final UsuarioService usuarioService;

    public HomeController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String username,
                                @RequestParam String password,
                                Model model) {

        Usuario usuario = usuarioService.validarLogin(username, password);

        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            return "panel";  // Página principal después del login
        } else {
            model.addAttribute("error", "Credenciales incorrectas");
            return "login";
        }
    }
}