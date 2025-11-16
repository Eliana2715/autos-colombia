package com.autoscolombia.parqueadero.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.autoscolombia.parqueadero.model.Usuario;
import com.autoscolombia.parqueadero.service.RegistroService;
import com.autoscolombia.parqueadero.service.UsuarioService;

@Controller
public class RegistroController {

    private final RegistroService registroService;
    private final UsuarioService usuarioService;

    public RegistroController(RegistroService registroService, UsuarioService usuarioService) {
        this.registroService = registroService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/registros")
    public String redirigirRegistros() {
        return "redirect:/registros/activos";
    }

    @GetMapping("/registros/activos")
    public String registrosActivos(Model model) {
        model.addAttribute("registros", registroService.listarActivos());
        return "registros/lista-registro";
    }

    @GetMapping("/registros/nuevo")
    public String nuevoRegistro() {
        return "registros/form-registros";
    }

    @PostMapping("/registros/entrada")
    public String registrarEntrada(
            @RequestParam String placa,
            @RequestParam String tipoVehiculo,
            @RequestParam Long usuarioId
    ) {
        // Buscar usuario real
        Usuario usuario = usuarioService.obtenerPorId(usuarioId);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        // Aquí podrías validar el rol si quieres:
        // if (!usuario.getRol().equals("ADMIN")) throw new RuntimeException("No tiene permisos");

        registroService.registrarEntrada(placa, tipoVehiculo, usuario);
        return "redirect:/registros/activos";
    }

    @GetMapping("/registros/salida/{id}")
    public String registrarSalida(@PathVariable Long id) {
        registroService.registrarSalida(id);
        return "redirect:/registros/activos";
    }
}
