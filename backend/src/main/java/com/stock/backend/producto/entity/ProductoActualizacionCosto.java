package com.stock.backend.producto.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "producto_actualizacion_costos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoActualizacionCosto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long actualizacion;

    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal costo;

    @Column(length = 20)
    private String moneda;

    public void actualizar(ProductoActualizacionCosto nuevosDatos) {
        if (nuevosDatos.getCosto() != null) {
            this.costo = nuevosDatos.getCosto();
        }
        if (nuevosDatos.getMoneda() != null) {
            this.moneda = nuevosDatos.getMoneda().trim();
        }
    }
}
