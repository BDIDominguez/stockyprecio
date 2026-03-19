package com.stock.backend.sucursal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sucursales")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sucursal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private Long codigo;
    @Column(nullable = false, length = 200)
    private String nombre;
    @Column(length = 255)
    private String direccion;
    @Column(length = 50)
    private String telefono;
    @Column(length = 100)
    private String encargado;
    @Builder.Default
    @Column(nullable = false)
    private Boolean activo = true;
    @Column(name = "fecha_creacion")
    private LocalDateTime creado;
    @Column(name = "fecha_modificacion")
    private LocalDateTime modificado;
    @Column(name = "usuario_creacion")
    private String creador;
    @Column(name = "usuario_modificacion")
    private String modificador;

    @PrePersist
    protected void inCreate(){
        this.creado = LocalDateTime.now();
        this.modificado = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        this.modificado = LocalDateTime.now();
    }

    public void actualizar(Sucursal datos){
        if (datos.codigo != null){
            this.codigo = datos.getCodigo();
        }
        if (datos.getNombre() != null) {
            this.nombre = datos.getNombre();
        }
        if (datos.getDireccion() != null) {
            this.direccion = datos.getDireccion();
        }
        if (datos.getTelefono() != null) {
            this.telefono = datos.getTelefono();
        }
        if (datos.getEncargado() != null) {
            this.encargado = datos.getEncargado();
        }
        if (datos.getActivo() != null) {
            this.activo = datos.getActivo();
        }

    }

}
