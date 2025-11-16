package com.autoscolombia.parqueadero.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import com.autoscolombia.parqueadero.model.Pago;
import com.autoscolombia.parqueadero.model.Registro;
import com.autoscolombia.parqueadero.repository.PagoRepository;
import com.autoscolombia.parqueadero.repository.RegistroRepository;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final RegistroRepository registroRepository;

    public PagoService(PagoRepository pagoRepository, RegistroRepository registroRepository) {
        this.pagoRepository = pagoRepository;
        this.registroRepository = registroRepository;
    }

    public void registrarPago(Long registroId, Double monto, String tipoPago) {
        Registro registro = registroRepository.findById(registroId)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));

        Pago pago = new Pago();
        pago.setRegistro(registro);
        pago.setMonto(monto);
        pago.setTipoPago(tipoPago); // Puede ser EFECTIVO o TARJETA
        pago.setFechaPago(LocalDateTime.now());

        pagoRepository.save(pago);

        System.out.println("Pago registrado con éxito. ID del registro: " + registroId);
    }
}
