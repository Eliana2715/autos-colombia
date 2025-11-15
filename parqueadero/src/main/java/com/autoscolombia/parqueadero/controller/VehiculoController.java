package com.autoscolombia.parqueadero.controller;

import com.autoscolombia.parqueadero.model.Celda;
import com.autoscolombia.parqueadero.model.Registro;
import com.autoscolombia.parqueadero.model.Usuario;
import com.autoscolombia.parqueadero.model.Vehiculo;
import com.autoscolombia.parqueadero.service.VehiculoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vehiculos")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @GetMapping
    public String listarVehiculos(Model model) {
        model.addAttribute("vehiculos", vehiculoService.listarVehiculosActivos());
        return "vehiculos";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("vehiculo", new Vehiculo());
        model.addAttribute("celdas", vehiculoService.listarCeldasLibres());
        model.addAttribute("usuarios", vehiculoService.listarUsuarios());
        return "vehiculo-form";
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
        Registro registro = vehiculoService.buscarRegistroActivoPorVehiculo(id);
        vehiculoService.calcularTiempoYValor(registro);
        model.addAttribute("registro", registro);
        return "vehiculo-salida";

    }

    @GetMapping("/editar/{id}")
    public String editarVehiculo(@PathVariable Long id, Model model) {
        model.addAttribute("vehiculo", vehiculoService.buscarPorId(id));
        return "vehiculo-form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarVehiculo(@PathVariable Long id) {
        vehiculoService.eliminarVehiculo(id);
        return "redirect:/vehiculos";
    }
    public List<Celda> listarCeldasLibres() {
    return celdaRepository.findAll().stream()
            .filter(c -> "libre".equalsIgnoreCase(c.getEstado()))
            .toList();
}

public List<Usuario> listarUsuarios() {
    return usuarioRepository.findAll();
}

public Registro buscarRegistroActivoPorVehiculo(Long vehiculoId) {
    return registroRepository.findByEstado("ABIERTA").stream()
            .filter(r -> r.getVehiculo().getVehiculoId().equals(vehiculoId))
            .findFirst()
            .orElseThrow();
}
}
