package com.autoscolombia.parqueadero.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.autoscolombia.parqueadero.model.Pago;
import com.autoscolombia.parqueadero.model.Registro;
import com.autoscolombia.parqueadero.model.Usuario;
import com.autoscolombia.parqueadero.repository.PagoRepository;
import com.autoscolombia.parqueadero.repository.RegistroRepository;
import com.autoscolombia.parqueadero.repository.UsuarioRepository;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final RegistroRepository registroRepository;
    private final UsuarioRepository usuarioRepository;

    public PagoService(PagoRepository pagoRepository, RegistroRepository registroRepository, UsuarioRepository usuarioRepository) {
        this.pagoRepository = pagoRepository;
        this.registroRepository = registroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Registrar pago
    public void registrarPago(Long registroId, Double monto, String tipoPago, Long usuarioId) {
        Registro registro = registroRepository.findById(registroId)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));

        if (!"CERRADA".equalsIgnoreCase(registro.getEstado())) {
        throw new RuntimeException("El registro debe estar cerrado para generar el pago");
        }

        // Asignar usuario por defecto (id=1)
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Pago pago = new Pago();
        pago.setRegistro(registro);
        pago.setUsuario(usuario);
        pago.setMonto(monto);
        pago.setTipoPago(tipoPago); // EFECTIVO o TARJETA
        pago.setFechaPago(LocalDateTime.now());

        pagoRepository.save(pago);

        System.out.println("Pago registrado con éxito. ID del registro: " + registroId);
    }

    // Listar todos los pagos
    public List<Pago> listarTodos() {
        return pagoRepository.findAll();
    }
}
