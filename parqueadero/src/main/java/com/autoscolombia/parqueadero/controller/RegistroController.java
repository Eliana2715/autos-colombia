package com.autoscolombia.parqueadero.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.autoscolombia.parqueadero.model.Registro;
import com.autoscolombia.parqueadero.service.CeldaService;
import com.autoscolombia.parqueadero.service.RegistroService;
import com.autoscolombia.parqueadero.service.UsuarioService;

@Controller
@RequestMapping("/registros")
public class RegistroController {

    private final RegistroService registroService;
    private final CeldaService celdaService;
    private final UsuarioService usuarioService;

    public RegistroController(RegistroService registroService,
                              CeldaService celdaService,
                              UsuarioService usuarioService) {
        this.registroService = registroService;
        this.celdaService = celdaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/activos")
    public String listarActivos(Model model) {
        List<Registro> registros = registroService.listarActivos();
        model.addAttribute("registros", registros);
        return "registros/lista-registro";
    }

    @GetMapping("/nuevo")
    public String formularioEntrada(Model model) {
        model.addAttribute("celdas", celdaService.listarDisponibles());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "registros/form-registros";
    }

    @PostMapping("/entrada")
    public String registrarEntrada(@RequestParam String placa,
                                   @RequestParam String tipoVehiculo,
                                   @RequestParam Long celdaId,
                                   @RequestParam Long usuarioId) {

        // 🔹 Cambio: usar RegistroService para centralizar la lógica
        registroService.registrarEntrada(placa, tipoVehiculo, celdaId, usuarioService.buscarPorId(usuarioId));

        return "redirect:/registros/activos";
    }

    @GetMapping("/salida/{id}")
    public String registrarSalida(@PathVariable Long id) {
        // 🔹 Cambio: usar RegistroService
        registroService.registrarSalida(id);
        return "redirect:/registros/activos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarRegistro(@PathVariable Long id) {
        registroService.eliminar(id);
        return "redirect:/registros/activos";
    }

    @GetMapping("/editar/{id}")
    public String editarRegistro(@PathVariable Long id, Model model) {
        Registro registro = registroService.buscarPorId(id);
        model.addAttribute("registro", registro);
        model.addAttribute("celdas", celdaService.listarDisponibles());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "registros/form-registros";
    }

    @PostMapping("/actualizar")
    public String actualizarRegistro(@RequestParam Long registroId,
                                     @RequestParam String placa,
                                     @RequestParam String tipoVehiculo,
                                     @RequestParam Long celdaId,
                                     @RequestParam Long usuarioId) {

        registroService.actualizar(registroId, placa, tipoVehiculo, celdaId, usuarioId);
        return "redirect:/registros/activos";
    }
}
