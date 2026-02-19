package com.stock.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true, nullable = false)
    private Long codigo;
    private String nombre;
    private String descripcion;
    private Long categoria;
    private Long proveedor;
    @Builder.Default
    private Double stockMinimo = 0.00;
    @Builder.Default
    private Boolean manejaStock = true;
    @Builder.Default
    private Boolean activo = true;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    private Long tipoIva;

    @PrePersist
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        this.fechaCreacion = now;
        this.fechaModificacion = now;
    }

    @PreUpdate
    public void preUpdate(){
        this.fechaModificacion = LocalDateTime.now();
    }

    public boolean isActivo(){
        return this.activo != null && this.activo;
    }

    public boolean manejaStock(){
        return this.manejaStock != null && this.manejaStock;
    }

    public void actualizarCampos(Producto nuevosDatos) {
        if (nuevosDatos.getNombre() != null) {
            this.nombre = nuevosDatos.getNombre();
        }
        if (nuevosDatos.getDescripcion() != null) {
            this.descripcion = nuevosDatos.getDescripcion();
        }
        if (nuevosDatos.getCategoria() != null) {
            this.categoria = nuevosDatos.getCategoria();
        }
        if (nuevosDatos.getProveedor() != null) {
            this.proveedor = nuevosDatos.getProveedor();
        }
        if (nuevosDatos.getStockMinimo() != null){
            this.stockMinimo = nuevosDatos.getStockMinimo();
        }
        if (nuevosDatos.getManejaStock() != null){
            this.manejaStock = nuevosDatos.getManejaStock();
        }
        if (nuevosDatos.getActivo() != null){
            this.activo = nuevosDatos.getActivo();
        }
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", activo=" + activo +
                '}';
    }
}
