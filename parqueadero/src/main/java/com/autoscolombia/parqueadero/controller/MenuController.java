package com.autoscolombia.parqueadero.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/menu")
public class MenuController {

    @GetMapping("/admin")
    public String adminMenu(HttpSession session) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/login";
        }
        return "menu"; // menu.html mostrará las opciones según rol
    }

    @GetMapping("/empleado")
    public String empleadoMenu(HttpSession session) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/login";
        }
        return "menu";
    }

    @GetMapping("/cajero")
    public String cajeroMenu(HttpSession session) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/login";
        }
        return "menu";
    }
}
