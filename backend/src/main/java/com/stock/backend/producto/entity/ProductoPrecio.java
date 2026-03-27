package com.stock.backend.producto.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "producto_precios",
        uniqueConstraints = @UniqueConstraint(columnNames = {"producto", "listaPrecio"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoPrecio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long producto;

    @Column(nullable = false)
    private Long listaPrecio;

    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal costoFinalReferencia;

    @Column(nullable = false, precision = 8, scale = 4)
    private BigDecimal margenPorcentaje;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precioVenta;

    @Builder.Default
    @Column(nullable = false)
    private Boolean activo = true;

    @Column(nullable = false)
    private LocalDateTime fechaCalculo;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.fechaCreacion = now;
        this.fechaModificacion = now;
        if (this.fechaCalculo == null) {
            this.fechaCalculo = now;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaModificacion = LocalDateTime.now();
    }

    public boolean isActivo() {
        return this.activo != null && this.activo;
    }

    public void actualizar(ProductoPrecio nuevosDatos) {
        if (nuevosDatos.getCostoFinalReferencia() != null) {
            this.costoFinalReferencia = nuevosDatos.getCostoFinalReferencia();
        }
        if (nuevosDatos.getMargenPorcentaje() != null) {
            this.margenPorcentaje = nuevosDatos.getMargenPorcentaje();
        }
        if (nuevosDatos.getPrecioVenta() != null) {
            this.precioVenta = nuevosDatos.getPrecioVenta();
        }
        if (nuevosDatos.getActivo() != null) {
            this.activo = nuevosDatos.getActivo();
        }
        if (nuevosDatos.getFechaCalculo() != null) {
            this.fechaCalculo = nuevosDatos.getFechaCalculo();
        }
    }
}
