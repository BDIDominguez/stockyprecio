package com.stock.backend.producto.dto;

import java.time.LocalDateTime;

public record ProductoDTO(
        Long codigo,
        String nombre,
        String descripcion,
        Long categoria,
        Long proveedor,
        Boolean manejaStock,
        Boolean activo,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaModificacion
) {
}
