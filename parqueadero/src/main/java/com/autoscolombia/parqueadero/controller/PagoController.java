package com.autoscolombia.parqueadero.controller;

import com.autoscolombia.parqueadero.service.PagoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping
    public String formularioPagos(Model model, HttpSession session) {
        return "pagos/form-pago"; // Vista Thymeleaf para el formulario del pago
    }

    @PostMapping
    public String registrarPago(
            @RequestParam Long registroId,
            @RequestParam Double monto,
            @RequestParam String tipoPago
    ) {
        pagoService.registrarPago(registroId, monto, tipoPago);
        return "redirect:/pagos?success=true";
    }
}
