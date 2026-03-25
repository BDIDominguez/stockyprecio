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

@Entity
@Table(name = "producto_actualizacion_datos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoActualizacionDato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long actualizacion;

    @Column(length = 200)
    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    private Long categoria;
    private Long proveedor;
    private Boolean activo;

    public void actualizar(ProductoActualizacionDato nuevosDatos) {
        if (nuevosDatos.getNombre() != null && !nuevosDatos.getNombre().trim().isEmpty()) {
            this.nombre = nuevosDatos.getNombre().trim();
        }
        if (nuevosDatos.getDescripcion() != null) {
            this.descripcion = nuevosDatos.getDescripcion().trim();
        }
        if (nuevosDatos.getCategoria() != null) {
            this.categoria = nuevosDatos.getCategoria();
        }
        if (nuevosDatos.getProveedor() != null) {
            this.proveedor = nuevosDatos.getProveedor();
        }
        if (nuevosDatos.getActivo() != null) {
            this.activo = nuevosDatos.getActivo();
        }
    }
}
