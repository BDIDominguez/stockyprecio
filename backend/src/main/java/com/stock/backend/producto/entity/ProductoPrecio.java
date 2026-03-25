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
        uniqueConstraints = @UniqueConstraint(columnNames = {"codigoProducto", "listaPrecio"})
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
    private Long codigoProducto;

    @Column(nullable = false, length = 30)
    private String listaPrecio;

    @Column(nullable = false, length = 20)
    private String modoCalculo;

    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal costoBase;

    @Column(precision = 8, scale = 4)
    private BigDecimal margenPorcentaje;

    @Column(precision = 12, scale = 4)
    private BigDecimal precioManual;

    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal subtotalAntesImpuestos;

    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal importeImpuestos;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precioFinal;

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

    public void actualizar(ProductoPrecio nuevosDatos) {
        if (nuevosDatos.getModoCalculo() != null && !nuevosDatos.getModoCalculo().trim().isEmpty()) {
            this.modoCalculo = nuevosDatos.getModoCalculo().trim();
        }
        if (nuevosDatos.getCostoBase() != null) {
            this.costoBase = nuevosDatos.getCostoBase();
        }
        if (nuevosDatos.getMargenPorcentaje() != null) {
            this.margenPorcentaje = nuevosDatos.getMargenPorcentaje();
        }
        if (nuevosDatos.getPrecioManual() != null) {
            this.precioManual = nuevosDatos.getPrecioManual();
        }
        if (nuevosDatos.getSubtotalAntesImpuestos() != null) {
            this.subtotalAntesImpuestos = nuevosDatos.getSubtotalAntesImpuestos();
        }
        if (nuevosDatos.getImporteImpuestos() != null) {
            this.importeImpuestos = nuevosDatos.getImporteImpuestos();
        }
        if (nuevosDatos.getPrecioFinal() != null) {
            this.precioFinal = nuevosDatos.getPrecioFinal();
        }
        if (nuevosDatos.getActivo() != null) {
            this.activo = nuevosDatos.getActivo();
        }
    }
}
