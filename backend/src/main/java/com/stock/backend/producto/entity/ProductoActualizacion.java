package com.stock.backend.producto.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "producto_actualizaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoActualizacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long codigoProducto;

    @Column(nullable = false, length = 20)
    private String estado;

    @Builder.Default
    @Column(nullable = false)
    private Boolean esProgramada = false;

    private LocalDateTime fechaProgramada;
    private LocalDateTime fechaAplicacion;

    @Column(length = 200)
    private String motivo;

    @Column(length = 1000)
    private String observacion;

    @Column(length = 100)
    private String usuario;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.fechaCreacion = now;
        this.fechaModificacion = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaModificacion = LocalDateTime.now();
    }

    public void actualizar(ProductoActualizacion nuevosDatos) {
        if (nuevosDatos.getEstado() != null && !nuevosDatos.getEstado().trim().isEmpty()) {
            this.estado = nuevosDatos.getEstado().trim();
        }
        if (nuevosDatos.getEsProgramada() != null) {
            this.esProgramada = nuevosDatos.getEsProgramada();
        }
        if (nuevosDatos.getFechaProgramada() != null) {
            this.fechaProgramada = nuevosDatos.getFechaProgramada();
        }
        if (nuevosDatos.getFechaAplicacion() != null) {
            this.fechaAplicacion = nuevosDatos.getFechaAplicacion();
        }
        if (nuevosDatos.getMotivo() != null) {
            this.motivo = nuevosDatos.getMotivo().trim();
        }
        if (nuevosDatos.getObservacion() != null) {
            this.observacion = nuevosDatos.getObservacion().trim();
        }
        if (nuevosDatos.getUsuario() != null) {
            this.usuario = nuevosDatos.getUsuario().trim();
        }
    }
}
