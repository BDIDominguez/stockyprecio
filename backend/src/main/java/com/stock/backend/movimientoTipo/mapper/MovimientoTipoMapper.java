package com.stock.backend.movimientoTipo.mapper;

import com.stock.backend.movimientoTipo.dto.MovimientoTipoDTO;
import com.stock.backend.movimientoTipo.entity.MovimientoTipo;

public class MovimientoTipoMapper {

    public static MovimientoTipoDTO toDto(MovimientoTipo mov){
        return new MovimientoTipoDTO(
            mov.getId(),
            mov.getSiglas(),
            mov.getNombre(),
            mov.getDescripcion(),
            mov.getAfectaStock(),
            mov.getRequiereSucursalOrigen(),
            mov.getRequiereSucursalDestino(),
            mov.getActivo()
        );
    }

    public static MovimientoTipo toEntidad(MovimientoTipoDTO dto){
        return new MovimientoTipo(
            dto.id(),
            dto.siglas(),
            dto.nombre(),
            dto.descripcion(),
            dto.afectaStock(),
            dto.requiereSucursalOrigen(),
            dto.requiereSucursalDestino(),
            dto.activo());
    }
}
