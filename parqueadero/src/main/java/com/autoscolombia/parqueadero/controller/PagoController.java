package com.autoscolombia.parqueadero.controller;

import com.autoscolombia.parqueadero.model.Registro;
import com.autoscolombia.parqueadero.service.PagoService;
import com.autoscolombia.parqueadero.service.RegistroService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/pagos")
public class PagoController {

    private final PagoService pagoService;
    private final RegistroService registroService; // 🔹 Cambiado

    public PagoController(PagoService pagoService, RegistroService registroService) {
        this.pagoService = pagoService;
        this.registroService = registroService;
    }

    // Mostrar todos los pagos
    @GetMapping
    public String listarPagos(Model model) {
        model.addAttribute("pagos", pagoService.listarTodos());
        return "pagos/pagos";
    }

    // Mostrar formulario de pago
    @GetMapping("/form-pago/{registroId}")
    public String mostrarFormularioPago(@PathVariable Long registroId, Model model) {
        Registro registro = registroService.buscarPorId(registroId);

        if (registro == null) {
            return "redirect:/pagos?error=RegistroNoExiste";
        }

        if (!"CERRADA".equalsIgnoreCase(registro.getEstado())) {
        throw new RuntimeException("El registro debe estar cerrado para generar el pago");
        }

        model.addAttribute("registro", registro);
        return "pagos/form-pago";
    }

    // Registrar pago
    @PostMapping("/registrar")
    public String registrarPago(@RequestParam Long registroId,
                                @RequestParam Double monto,
                                @RequestParam String tipoPago,
                                @RequestParam Long usuarioId) {
        pagoService.registrarPago(registroId, monto, tipoPago, usuarioId);
        return "redirect:/pagos?success";
    }
}
