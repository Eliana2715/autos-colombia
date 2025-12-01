package com.autoscolombia.parqueadero.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.autoscolombia.parqueadero.model.Celda;
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

    public RegistroController(RegistroService registroService, CeldaService celdaService, UsuarioService usuarioService) {
        this.registroService = registroService;
        this.celdaService = celdaService;
        this.usuarioService = usuarioService;
    }

    
    @GetMapping("/activos")
    public String listarActivos(Model model) {
        List<Registro> registrosActivos = registroService.listarActivos();
        model.addAttribute("registros", registrosActivos);
        return "registros/lista-registro";
    }


    @GetMapping("/nuevo")
    public String nuevoRegistro(Model model) {
        model.addAttribute("registro", new Registro());
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


    @PostMapping("/guardar")
    public String guardarRegistro(@ModelAttribute Registro registro,
                                @RequestParam Long celdaId,
                                @RequestParam Long usuarioId) {

        registroService.guardar(registro, celdaId, usuarioId);
        return "redirect:/registros/activos";
    }

    @GetMapping("/editar/{id}")
    public String editarRegistro(@PathVariable Long id, Model model) {
        Registro registro = registroService.buscarPorId(id);
        List<Celda> celdasDisponibles = celdaService.listarDisponibles();
        if (registro.getCelda() != null && !celdasDisponibles.contains(registro.getCelda())) {
            celdasDisponibles.add(registro.getCelda());
        }

        model.addAttribute("registro", registro);
        model.addAttribute("celdas", celdasDisponibles);
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "registros/form-registros";
    }

    
    @PostMapping("/actualizar")
    public String actualizarRegistro(@RequestParam Long registroId,
                                    @RequestParam("vehiculo.placa") String placa,
                                    @RequestParam("vehiculo.tipo") String tipoVehiculo,
                                    @RequestParam Long celdaId,
                                    @RequestParam Long usuarioId) {

        registroService.actualizar(registroId, placa, tipoVehiculo, celdaId, usuarioId);
        return "redirect:/registros/activos";
    }

    @GetMapping("/salida/{id}")
    public String registrarSalida(@PathVariable Long id) {
        registroService.registrarSalida(id);
        return "redirect:/registros/activos"; // vuelve a la lista de registros activos
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarRegistro(@PathVariable Long id) {
        registroService.eliminar(id);
        return "redirect:/registros/activos";
    }

}
