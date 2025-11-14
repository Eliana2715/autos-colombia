package com.autoscolombia.parqueadero.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Registro {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long registroId;

    @ManyToOne
    private Vehiculo vehiculo;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Celda celda;

    private LocalDateTime fechaIngreso;
    private LocalDateTime fechaSalida;
    private String estado; // ABIERTA, CERRADA

    public void cerrarRegistro(LocalDateTime salida){
        this.fechaSalida = salida;
        this.estado = "CERRADA";
    }

    public Long getRegistroId() {
        return registroId;
    }

    public void setRegistroId(Long registroId) {
        this.registroId = registroId;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Celda getCelda() {
        return celda;
    }

    public void setCelda(Celda celda) {
        this.celda = celda;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDateTime getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDateTime fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    } 

    
}
