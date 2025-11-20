package com.stock.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "movimiento_tipo")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovimientoTipo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Código corto: COMP, VENT, TRF, AJST...
    private String siglas;
    // Nombre descriptivo: "Compra a proveedor", "Transferencia entre sucursales"
    private String nombre;
    // Descripción opcional
    private String descripcion;
    // 1 = suma, -1 = resta, 0 = no afecta, 2 = transferencia (resta origen / suma destino)
    private Integer afectaStock;
    // Flags para saber qué sucursales deben especificarse
    private Boolean requiereSucursalOrigen;
    private Boolean requiereSucursalDestino;
    // Para poder activar/desactivar tipos sin borrarlos
    private Boolean activo;

    @Override
    public String toString() {
        return "MovimientoTipo{" +
                "id=" + id +
                ", siglas='" + siglas + '\'' +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", afectaStock=" + afectaStock +
                ", requiereSucursalOrigen=" + requiereSucursalOrigen +
                ", requiereSucursalDestino=" + requiereSucursalDestino +
                ", activo=" + activo +
                '}';
    }
}
