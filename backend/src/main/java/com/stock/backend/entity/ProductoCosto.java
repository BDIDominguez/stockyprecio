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
@Table(name = "producto_costo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoCosto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long Codigo;

    @Column(nullable = false)
    private Long sucursal;

    @Column(nullable = false, length = 100)
    private String concepto; // flete, merma, embalaje, etc.

    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal valor;

    // true = importe fijo | false = porcentaje
    @Column(nullable = false)
    private Boolean esFijo;

    // Indica si aplica sobre neto o sobre costo base
    @Column(nullable = false)
    private Boolean aplicaSobreNeto;

    @Builder.Default
    private Boolean activo = true;

    private LocalDateTime fechaCreacion;
    
}
