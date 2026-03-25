package com.stock.backend.listaPrecio.dto;

import java.time.LocalDateTime;

public record ListaPrecioDTO(
        Long id,
        String codigo,
        String nombre,
        String descripcion,
        Boolean activo,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaModificacion
) {
}
