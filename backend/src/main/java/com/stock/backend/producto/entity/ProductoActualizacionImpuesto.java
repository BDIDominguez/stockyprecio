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

@Entity
@Table(
        name = "producto_actualizacion_impuestos",
        uniqueConstraints = @UniqueConstraint(columnNames = {"actualizacion", "impuesto"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoActualizacionImpuesto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long actualizacion;

    @Column(nullable = false)
    private Long impuesto;

    @Column(nullable = false)
    private Integer ordenAplicacion;

    public void actualizar(ProductoActualizacionImpuesto nuevosDatos) {
        if (nuevosDatos.getImpuesto() != null) {
            this.impuesto = nuevosDatos.getImpuesto();
        }
        if (nuevosDatos.getOrdenAplicacion() != null) {
            this.ordenAplicacion = nuevosDatos.getOrdenAplicacion();
        }
    }
}
