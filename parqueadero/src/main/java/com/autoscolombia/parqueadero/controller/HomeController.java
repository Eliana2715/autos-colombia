package com.autoscolombia.parqueadero.controller;

import com.autoscolombia.parqueadero.model.Usuario;
import com.autoscolombia.parqueadero.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // login.html
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {

        Usuario usuario = usuarioService.validarUsuario(username, password)
                                        .orElse(null);

        if (usuario == null) {
            model.addAttribute("error", "Usuario o contraseña incorrecta");
            return "login";
        }

        // Guardar usuario en sesión
        session.setAttribute("usuario", usuario);

        // Redirigir según rol
        switch (usuario.getRol()) {
            case "ADMIN":
                return "redirect:/menu/admin";
            case "EMPLEADO":
                return "redirect:/menu/empleado";
            case "CAJERO":
                return "redirect:/menu/cajero";
            default:
                return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
