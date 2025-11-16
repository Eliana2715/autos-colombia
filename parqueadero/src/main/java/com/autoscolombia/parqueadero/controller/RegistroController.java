package com.autoscolombia.parqueadero.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.autoscolombia.parqueadero.model.Celda;
import com.autoscolombia.parqueadero.model.Usuario;
import com.autoscolombia.parqueadero.service.CeldaService;
import com.autoscolombia.parqueadero.service.RegistroService;
import com.autoscolombia.parqueadero.service.UsuarioService;

@Controller
public class RegistroController {

    private final RegistroService registroService;
    private final CeldaService celdaService;
    private final UsuarioService usuarioService;

    public RegistroController(RegistroService registroService, CeldaService celdaService, UsuarioService usuarioService) {
        this.registroService = registroService;
        this.celdaService = celdaService;
        this.usuarioService = usuarioService;
    }

    // 1️⃣ Mostrar registros activos
    @GetMapping("/registros/activos")
    public String registrosActivos(Model model) {
        model.addAttribute("registros", registroService.listarActivos());
        return "registros/lista-registro";
    }

    // 2️⃣ Formulario para nuevo registro
    @GetMapping("/registros/nuevo")
    public String nuevoRegistro() {
        return "registros/form-registros"; // solo formulario con campo placa
    }

    // 3️⃣ Registrar entrada
    @PostMapping("/registros/entrada")
    public String registrarEntrada(@RequestParam String placa) {
        // Buscar celda libre automáticamente
        Celda celda = celdaService.listarDisponibles().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No hay celdas disponibles"));

        // Usar un usuario predeterminado
        Usuario usuario = usuarioService.obtenerPorId(1L);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        // Registrar entrada
        registroService.registroEntrada(placa, "DESCONOCIDO", celda, usuario);

        return "redirect:/registros/activos";
    }

    //  Registrar salida
    @GetMapping("/registros/salida/{id}")
    public String registrarSalida(@PathVariable Long id) {
        registroService.registrarSalida(id);
        return "redirect:/registros/activos";
    }
}
