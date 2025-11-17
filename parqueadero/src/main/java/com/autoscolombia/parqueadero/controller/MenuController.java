package com.autoscolombia.parqueadero.controller;

import com.autoscolombia.parqueadero.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/menu")
public class MenuController {

    // Mapping genérico por si alguien entra a /menu directamente
    @GetMapping
    public String menu(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        return "menu"; // menu.html mostrará opciones según rol
    }

    @GetMapping("/admin")
    public String adminMenu(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        return "menu";
    }

    @GetMapping("/empleado")
    public String empleadoMenu(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        return "menu";
    }

    @GetMapping("/cajero")
    public String cajeroMenu(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        return "menu";
    }
}
