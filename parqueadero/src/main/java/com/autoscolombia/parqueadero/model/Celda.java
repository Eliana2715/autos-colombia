package com.autoscolombia.parqueadero.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "celdas")
public class Celda {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long celdaId;

    @Column(unique = true)

    private String codigo; 
    private String tipo;
    private String estado; // libre - ocupado
    
    public Long getCeldaId() {
        return celdaId;
    }
    public void setCeldaId(Long celdaId) {
        this.celdaId = celdaId;
    }
    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }

    
    
    
}
