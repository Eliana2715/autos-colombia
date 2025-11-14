package com.autoscolombia.parqueadero.service;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.autoscolombia.parqueadero.model.Celda;
import com.autoscolombia.parqueadero.model.Registro;
import com.autoscolombia.parqueadero.model.Vehiculo;
import com.autoscolombia.parqueadero.repository.CeldaRepository;
import com.autoscolombia.parqueadero.repository.RegistroRepository;
import com.autoscolombia.parqueadero.repository.VehiculoRepository;

@Service
public class RegistroService {
    private final RegistroRepository registroRepo;
    private final VehiculoRepository vehRepo;
    private final CeldaRepository celdaRepo;

    public RegistroService(RegistroRepository registroRepo, VehiculoRepository vehRepo, CeldaRepository celdaRepo){
        this.registroRepo = registroRepo;
        this.vehRepo = vehRepo;
        this.celdaRepo = celdaRepo;
    }

    public Registro registrarEntrada(String placa, String tipoVeh, String codigoCelda, Long usuarioId){
        Vehiculo v = vehRepo.findByPlaca(placa).orElseGet(() -> {
            Vehiculo nv = new Vehiculo();
            nv.setPlaca(placa);
            nv.setTipo(tipoVeh);
            return vehRepo.save(nv);
        });

        Celda celda = null;
        if(codigoCelda != null && !codigoCelda.isEmpty()){
            celda = celdaRepo.findByCodigo(codigoCelda).orElse(null);
            if(celda != null){
                celda.setEstado("OCUPADA");
                celdaRepo.save(celda);
            } else {
                // opcional: lanzar excepción o manejar asignación automática
            }
        }

        Registro r = new Registro();
        r.setVehiculo(v);
        r.setCelda(celda);
        r.setFechaIngreso(LocalDateTime.now());
        r.setEstado("ABIERTA");
        return registroRepo.save(r);
    }

    public Registro registrarSalida(Long registroId){
        Registro r = registroRepo.findById(registroId)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));
        r.cerrarRegistro(LocalDateTime.now());
        if(r.getCelda() != null){
            Celda c = r.getCelda();
            c.setEstado("LIBRE");
            celdaRepo.save(c);
        }
        return registroRepo.save(r);
    }

    public List<Registro> listarActivos(){
        return registroRepo.findByEstado("ABIERTA");
    }
}
