package com.stock.backend.sucursal.dto;

import java.time.LocalDateTime;

public record SucursalNuevaDTO(
        Long codigo,
        String nombre,
        String direccion,
        String telefono,
        String encargado,
        LocalDateTime creado,
        LocalDateTime modificado,
        String creador,
        String modificador
) {}

