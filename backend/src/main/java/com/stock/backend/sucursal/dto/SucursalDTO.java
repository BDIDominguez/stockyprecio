package com.stock.backend.sucursal.dto;

import com.stock.backend.sucursal.entity.Sucursal;

import java.time.LocalDateTime;

public record SucursalDTO (
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

