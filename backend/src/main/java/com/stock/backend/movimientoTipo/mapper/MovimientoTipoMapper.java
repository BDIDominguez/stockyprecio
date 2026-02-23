package com.stock.backend.movimientoTipo.mapper;

import org.mapstruct.Mapper;

import com.stock.backend.movimientoTipo.dto.MovimientoTipoDTO;
import com.stock.backend.movimientoTipo.entity.MovimientoTipo;

@Mapper(componentModel = "spring")
public interface MovimientoTipoMapper {

    MovimientoTipoDTO toDto(MovimientoTipo mov);

    MovimientoTipo toEntidad(MovimientoTipoDTO dto);
}