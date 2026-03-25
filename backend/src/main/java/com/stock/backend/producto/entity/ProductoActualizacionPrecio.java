package com.stock.backend.producto.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(
        name = "producto_actualizacion_precios",
        uniqueConstraints = @UniqueConstraint(columnNames = {"actualizacion", "listaPrecio"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoActualizacionPrecio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long actualizacion;

    @Column(nullable = false)
    private Long listaPrecio;

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

    public void actualizar(ProductoActualizacionPrecio nuevosDatos) {
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
    }
}
