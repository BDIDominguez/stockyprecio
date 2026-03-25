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

import java.time.LocalDateTime;

@Entity
@Table(
        name = "producto_impuestos",
        uniqueConstraints = @UniqueConstraint(columnNames = {"codigoProducto", "impuesto"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoImpuesto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long codigoProducto;

    @Column(nullable = false)
    private Long impuesto;

    @Column(nullable = false)
    private Integer ordenAplicacion;

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

    public void actualizar(ProductoImpuesto nuevosDatos) {
        if (nuevosDatos.getImpuesto() != null) {
            this.impuesto = nuevosDatos.getImpuesto();
        }
        if (nuevosDatos.getOrdenAplicacion() != null) {
            this.ordenAplicacion = nuevosDatos.getOrdenAplicacion();
        }
        if (nuevosDatos.getActivo() != null) {
            this.activo = nuevosDatos.getActivo();
        }
    }
}
