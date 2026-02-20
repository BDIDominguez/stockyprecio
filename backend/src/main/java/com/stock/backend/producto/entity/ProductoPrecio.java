package com.stock.backend.producto.entity;

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
@Table(name = "producto_precio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoPrecio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long Codigo;

    @Column(nullable = false)
    private Long sucursal;

    // margen sobre costo
    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal margen;

    @Builder.Default
    private Boolean activo = true;

    private LocalDateTime fechaCreacion;
}
