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
@Table(name = "tipo_iva")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoIva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Código lógico (EXE, RED, GEN o 0/1/2 si querés)
    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String descripcion;

    // 0.00 | 10.50 | 21.00
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal porcentaje;

    @Builder.Default
    @Column(nullable = false)
    private Boolean activo = true;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    
}
