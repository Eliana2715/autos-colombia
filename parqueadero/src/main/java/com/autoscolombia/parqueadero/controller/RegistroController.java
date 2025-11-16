package com.autoscolombia.parqueadero.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.autoscolombia.parqueadero.service.CeldaService;
import com.autoscolombia.parqueadero.service.RegistroService;

@Controller
public class RegistroController {

    private final RegistroService registroService;
    private final CeldaService celdaService;

    public RegistroController(RegistroService registroService, CeldaService celdaService) {
        this.registroService = registroService;
        this.celdaService = celdaService;
    }

    @GetMapping("/registros")
    public String redirigirRegistros() {
    return "redirect:/registros/activos";
    }

    // Mostrar registros activos (vehículos dentro del parqueadero)
    @GetMapping("/registros/activos")
    public String registrosActivos(Model model) {
        model.addAttribute("registros", registroService.listarActivos());
        return "registros/lista-registro";
    }

    // Mostrar formulario de ingreso de vehículo
    @GetMapping("/registros/nuevo")
    public String nuevoRegistro(Model model) {
        model.addAttribute("celdas", celdaService.listarDisponibles());
        return "registros/form-registros";
    }

    // Procesar entrada
    @PostMapping("/registros/entrada")
    public String registrarEntrada(
            @RequestParam String placa,
            @RequestParam String tipoVehiculo,
            @RequestParam(required = false) String celdaCodigo,
            @RequestParam Long usuarioId
    ) {
        registroService.registrarEntrada(placa, tipoVehiculo, celdaCodigo, usuarioId);
        return "redirect:/registros/activos";
    }

    @GetMapping("/registros/salida/{id}")
    public String registrarSalida(@PathVariable Long id) {
        registroService.registrarSalida(id);
        return "redirect:/registros/activos";
    }

}

