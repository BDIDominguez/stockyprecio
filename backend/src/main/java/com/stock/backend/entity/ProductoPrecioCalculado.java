package com.stock.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

@Entity
@Table(name = "producto_precio_calculado")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoPrecioCalculado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long Codigo;
    private Long sucursal;

    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal costoTotal;

    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal precioSinIva;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal ivaPorcentaje;

    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal ivaImporte;

    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal precioConIva;

    private LocalDateTime fechaCalculo;
}
