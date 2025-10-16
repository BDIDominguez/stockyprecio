package com.stock.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sucursales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sucursal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long codigo;
    private String nombre;
    private String direccion;
    private String telefono;
    private String encargado;
    private Boolean activo;
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
