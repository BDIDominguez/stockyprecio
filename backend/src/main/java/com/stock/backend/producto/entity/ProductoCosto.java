package com.stock.backend.producto.entity;

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
@Table(name = "producto_costos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoCosto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long producto;

    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal costoBase;

    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal neto1;

    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal neto2;

    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal costoFinal;

    @Builder.Default
    @Column(nullable = false, length = 20)
    private String moneda = "ARS";

    @Column(nullable = false)
    private LocalDateTime fechaCalculo;

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

    public void actualizar(ProductoCosto nuevosDatos) {
        if (nuevosDatos.getCostoBase() != null) {
            this.costoBase = nuevosDatos.getCostoBase();
        }
        if (nuevosDatos.getNeto1() != null) {
            this.neto1 = nuevosDatos.getNeto1();
        }
        if (nuevosDatos.getNeto2() != null) {
            this.neto2 = nuevosDatos.getNeto2();
        }
        if (nuevosDatos.getCostoFinal() != null) {
            this.costoFinal = nuevosDatos.getCostoFinal();
        }
        if (nuevosDatos.getMoneda() != null) {
            this.moneda = nuevosDatos.getMoneda().trim();
        }
        if (nuevosDatos.getFechaCalculo() != null) {
            this.fechaCalculo = nuevosDatos.getFechaCalculo();
        }
        if (nuevosDatos.getActivo() != null) {
            this.activo = nuevosDatos.getActivo();
        }
    }
}
