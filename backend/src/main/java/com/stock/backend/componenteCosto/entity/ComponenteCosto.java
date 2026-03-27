package com.stock.backend.componenteCosto.entity;

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
@Table(name = "componentes_costo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComponenteCosto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long codigo;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false, length = 30)
    private String tipoComponente;

    @Column(nullable = false, length = 20)
    private String tipoValor;

    @Builder.Default
    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal valorDefecto = BigDecimal.ZERO.setScale(4);

    @Column(nullable = false, length = 40)
    private String etapaAplicacion;

    @Builder.Default
    @Column(nullable = false)
    private Boolean editableEnProducto = true;

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
        if (this.valorDefecto == null) {
            this.valorDefecto = BigDecimal.ZERO.setScale(4);
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaModificacion = LocalDateTime.now();
    }

    public boolean isActivo() {
        return this.activo != null && this.activo;
    }

    public void actualizar(ComponenteCosto nuevosDatos) {
        if (nuevosDatos.getCodigo() != null) {
            this.codigo = nuevosDatos.getCodigo();
        }
        if (nuevosDatos.getNombre() != null && !nuevosDatos.getNombre().trim().isEmpty()) {
            this.nombre = nuevosDatos.getNombre().trim();
        }
        if (nuevosDatos.getDescripcion() != null) {
            this.descripcion = nuevosDatos.getDescripcion().trim();
        }
        if (nuevosDatos.getTipoComponente() != null && !nuevosDatos.getTipoComponente().trim().isEmpty()) {
            this.tipoComponente = nuevosDatos.getTipoComponente().trim();
        }
        if (nuevosDatos.getTipoValor() != null && !nuevosDatos.getTipoValor().trim().isEmpty()) {
            this.tipoValor = nuevosDatos.getTipoValor().trim();
        }
        if (nuevosDatos.getValorDefecto() != null) {
            this.valorDefecto = nuevosDatos.getValorDefecto();
        }
        if (nuevosDatos.getEtapaAplicacion() != null && !nuevosDatos.getEtapaAplicacion().trim().isEmpty()) {
            this.etapaAplicacion = nuevosDatos.getEtapaAplicacion().trim();
        }
        if (nuevosDatos.getEditableEnProducto() != null) {
            this.editableEnProducto = nuevosDatos.getEditableEnProducto();
        }
        if (nuevosDatos.getActivo() != null) {
            this.activo = nuevosDatos.getActivo();
        }
    }
}
