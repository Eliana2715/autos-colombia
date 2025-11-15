package com.autoscolombia.parqueadero.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.autoscolombia.parqueadero.model.Celda;
import com.autoscolombia.parqueadero.service.CeldaService;

@Controller
@RequestMapping("/celdas")
public class CeldaController {

    private final CeldaService celdaService;

    public CeldaController(CeldaService celdaService) {
        this.celdaService = celdaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("celdas", celdaService.listar());
        return "celdas/lista-celdas"; // luego creamos este HTML
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("celda", new Celda());
        return "celdas/form-celda"; // pagina para registrar/editar
    }

    @PostMapping
    public String guardar(@ModelAttribute Celda celda) {
        celdaService.guardar(celda);
        return "redirect:/celdas";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Celda celda = celdaService.buscarPorId(id);

        if (celda == null) {
            return "redirect:/celdas";
        }

        model.addAttribute("celda", celda);
        return "celdas/form-celda";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        celdaService.eliminar(id);
        return "redirect:/celdas";
    }
}

