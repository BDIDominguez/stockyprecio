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
@Table(name = "codigos_barras")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodigoDeBarra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long codigoProducto;

    @Column(nullable = false, unique = true, length = 100)
    private String barra;

    public void actualizar(CodigoDeBarra nuevosDatos) {
        if (nuevosDatos.getBarra() != null && !nuevosDatos.getBarra().trim().isEmpty()) {
            this.barra = nuevosDatos.getBarra().trim();
        }
    }
}
