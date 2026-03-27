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

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "producto_componentes_costo",
        uniqueConstraints = @UniqueConstraint(columnNames = {"producto", "componente"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoComponenteCosto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long producto;

    @Column(nullable = false)
    private Long componente;

    @Builder.Default
    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal valorAplicado = BigDecimal.ZERO.setScale(4);

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
        if (this.valorAplicado == null) {
            this.valorAplicado = BigDecimal.ZERO.setScale(4);
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaModificacion = LocalDateTime.now();
    }
}
