package com.autoscolombia.parqueadero.controller;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.autoscolombia.parqueadero.model.Registro;
import com.autoscolombia.parqueadero.model.Vehiculo;
import com.autoscolombia.parqueadero.service.RegistroService;
import com.autoscolombia.parqueadero.service.VehiculoService;

@Controller
@RequestMapping("/vehiculos")
public class VehiculoController {

    private final VehiculoService vehiculoService;
    private final RegistroService registroService; // 🔹 Inyectado

    public VehiculoController(VehiculoService vehiculoService, RegistroService registroService) {
        this.vehiculoService = vehiculoService;
        this.registroService = registroService;
    }

    @GetMapping
    public String listarVehiculos(Model model) {
        model.addAttribute("vehiculos", vehiculoService.listarVehiculosActivos());
        return "vehiculo/vehiculos";
    }

    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("vehiculo", new Vehiculo());
        model.addAttribute("celdas", vehiculoService.listarCeldasLibres());
        model.addAttribute("usuarios", vehiculoService.listarUsuarios());
        return "vehiculo/vehiculo-form";
    }

    @PostMapping("/guardar")
    public String guardarVehiculo(@ModelAttribute Vehiculo vehiculo,
                                @RequestParam Long usuarioId,
                                @RequestParam Long celdaId) {
        vehiculoService.registrarEntrada(vehiculo, usuarioId, celdaId);
        return "redirect:/vehiculos";
    }
    
    @GetMapping("/salida/{id}")
    public String mostrarSalida(@PathVariable Long id, Model model) {
        Registro registro = registroService.buscarPorId(id);

        if (!"ABIERTA".equalsIgnoreCase(registro.getEstado())) {
            throw new RuntimeException("El registro no está activo o ya fue cerrado");
        }

        // Hora de salida actual
        LocalDateTime horaSalida = LocalDateTime.now();
        model.addAttribute("registro", registro);
        model.addAttribute("horaSalida", horaSalida);

        return "vehiculo/vehiculo-salida";
    }

    @PostMapping("/salida")
    public String procesarSalida(@RequestParam Long registroId) {
        registroService.registrarSalida(registroId); // 🔹 Todo el cálculo ocurre aquí
        return "redirect:/vehiculos";
    }

    @GetMapping("/editar/{id}")
    public String editarVehiculo(@PathVariable Long id, Model model) {
        model.addAttribute("vehiculo", vehiculoService.buscarPorId(id));
        return "vehiculo/vehiculo-form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarVehiculo(@PathVariable Long id) {
        vehiculoService.eliminarVehiculo(id);
        return "redirect:/vehiculos";
    }
}
