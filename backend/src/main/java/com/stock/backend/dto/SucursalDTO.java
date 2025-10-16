package com.stock.backend.dto;

import com.stock.backend.entity.Sucursal;

import java.time.LocalDateTime;

public record SucursalDTO (
        Long id,
        Long codigo,
        String nombre,
        String direccion,
        String telefono,
        String encargado,
        Boolean activo,
        LocalDateTime creado,
        LocalDateTime modificado,
        String creador,
        String modificador
) {
    public SucursalDTO(Sucursal sucursal) {
        this(
                sucursal.getId(),
                sucursal.getCodigo(),
                sucursal.getNombre(),
                sucursal.getDireccion(),
                sucursal.getTelefono(),
                sucursal.getEncargado(),
                sucursal.getActivo(),
                sucursal.getCreado(),
                sucursal.getModificado(),
                sucursal.getCreador(),
                sucursal.getModificador()
        );
    }
}
