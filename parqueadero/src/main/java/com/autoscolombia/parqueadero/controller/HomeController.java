package com.autoscolombia.parqueadero.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.autoscolombia.parqueadero.service.UsuarioService;
import com.autoscolombia.parqueadero.model.Usuario;
import java.util.Optional;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class HomeController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // tu login.html
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                            @RequestParam String password,
                            HttpSession session,
                            Model model) {

        Optional<Usuario> usuarioOpt = usuarioService.validarUsuario(username, password);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            session.setAttribute("usuario", usuario); // guardamos usuario en sesión

            // Redirige según rol
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
        } else {
            model.addAttribute("error", "Usuario o contraseña incorrecta");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}