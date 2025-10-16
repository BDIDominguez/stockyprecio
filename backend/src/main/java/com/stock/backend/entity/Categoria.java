package com.stock.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "categorias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private Long codigo;
    @Column(nullable = false, length = 100)
    private String nombre;
    private String descripcion;
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

    public void actualizar(Categoria nuevosDatos) {
        if (nuevosDatos.getNombre() != null && !nuevosDatos.getNombre().trim().isEmpty()) {
            this.nombre = nuevosDatos.getNombre().trim();
        }
        if (nuevosDatos.getDescripcion() != null) {
            this.descripcion = nuevosDatos.getDescripcion().trim();
        }
        if (nuevosDatos.getActivo() != null) {
            this.activo = nuevosDatos.getActivo();
        }
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", activo=" + activo +
                '}';
    }
}
