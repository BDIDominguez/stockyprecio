package com.stock.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "proveedores", indexes = {@Index(name = "idx_proveedores_codigo", columnList = "codigo")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private Long codigo;
    @Column(length = 200, nullable = false)
    private String nombre;
    @Column(length = 100)
    private String contacto;
    @Column(length = 50)
    private String telefono;
    @Column(length = 100)
    private String email;
    @Column(length = 500)
    private String direccion;
    // Estado
    @Builder.Default
    @Column(nullable = false)
    private Boolean activo = true;

    // Auditoría
    @Column(name = "fecha_creacion", updatable = false, nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @Column(length = 50, name = "usuario_creacion", updatable = false)
    private String usuarioCreacion;

    @Column(length = 50, name = "usuario_modificacion")
    private String usuarioModificacion;

    // Callbacks para auditoría automática
    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaModificacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaModificacion = LocalDateTime.now();
    }

    public void actualizar(Proveedor actualizado) {
        // No actualizamos el ID ni la fecha de creación
        if (actualizado.getCodigo() != null) {
            this.codigo = actualizado.getCodigo();
        }
        if (actualizado.getNombre() != null) {
            this.nombre = actualizado.getNombre();
        }
        if (actualizado.getContacto() != null) {
            this.contacto = actualizado.getContacto();
        }
        if (actualizado.getTelefono() != null) {
            this.telefono = actualizado.getTelefono();
        }
        if (actualizado.getEmail() != null) {
            this.email = actualizado.getEmail();
        }
        if (actualizado.getDireccion() != null) {
            this.direccion = actualizado.getDireccion();
        }
        if (actualizado.getActivo() != null) {
            this.activo = actualizado.getActivo();
        }

        // Auditoría → solo se setea usuario_modificacion desde el objeto recibido
        if (actualizado.getUsuarioModificacion() != null) {
            this.usuarioModificacion = actualizado.getUsuarioModificacion();
        }

        // La fecha_modificacion se actualiza sola por @PreUpdate
    }
}
