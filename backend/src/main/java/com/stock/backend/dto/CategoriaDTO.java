package com.stock.backend.dto;

import java.time.LocalDateTime;

public record CategoriaDTO(
        Long id,
        Long codigo,
        String nombre,
        String descripcion,
        Boolean activo,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaModificacion
){
}
