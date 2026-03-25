package com.stock.backend.impuesto.entity;

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

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "impuestos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Impuesto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false, precision = 8, scale = 4)
    private BigDecimal porcentaje;

    @Builder.Default
    @Column(nullable = false)
    private Boolean activo = true;

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

    public boolean isActivo() {
        return this.activo != null && this.activo;
    }

    public void actualizar(Impuesto nuevosDatos) {
        if (nuevosDatos.getCodigo() != null && !nuevosDatos.getCodigo().trim().isEmpty()) {
            this.codigo = nuevosDatos.getCodigo().trim();
        }
        if (nuevosDatos.getNombre() != null && !nuevosDatos.getNombre().trim().isEmpty()) {
            this.nombre = nuevosDatos.getNombre().trim();
        }
        if (nuevosDatos.getDescripcion() != null) {
            this.descripcion = nuevosDatos.getDescripcion().trim();
        }
        if (nuevosDatos.getPorcentaje() != null) {
            this.porcentaje = nuevosDatos.getPorcentaje();
        }
        if (nuevosDatos.getActivo() != null) {
            this.activo = nuevosDatos.getActivo();
        }
    }
}
