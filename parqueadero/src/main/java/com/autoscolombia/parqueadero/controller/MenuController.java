package com.autoscolombia.parqueadero.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/menu")
public class MenuController {

    @GetMapping("/admin")
    public String adminMenu() {
        return "menu";
    }

    @GetMapping("/empleado")
    public String empleadoMenu() {
        return "menu";
    }

    @GetMapping("/cajero")
    public String cajeroMenu() {
        return "menu";
    }
}