package com.stock.backend.dto;

import java.time.LocalDateTime;

public record ProductoDTO(
        Long id,
        Long codigo,
        String nombre,
        String descripcion,
        Long categoria,
        Long proveedor,
        Double stockMinimo ,
        Boolean manejaStock ,
        Boolean activo,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaModificacion
        ) {}
