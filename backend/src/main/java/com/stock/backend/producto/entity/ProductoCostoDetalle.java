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
import java.time.LocalDateTime;

@Entity
@Table(
        name = "producto_costo_detalles",
        uniqueConstraints = @UniqueConstraint(columnNames = {"producto", "componente"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoCostoDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long producto;

    @Column(nullable = false)
    private Long componente;

    @Column(nullable = false, length = 40)
    private String etapaAplicacion;

    @Column(nullable = false, length = 40)
    private String resultadoEtapa;

    @Column(nullable = false, length = 20)
    private String tipoValorAplicado;

    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal valorAplicado;

    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal baseCalculo;

    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal importeCalculado;

    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal subtotalResultante;

    @Column(nullable = false)
    private LocalDateTime fechaCalculo;
}
