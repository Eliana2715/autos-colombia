package com.autoscolombia.parqueadero.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Sistema de Parqueadero iniciado correctamente!";
    }
}
