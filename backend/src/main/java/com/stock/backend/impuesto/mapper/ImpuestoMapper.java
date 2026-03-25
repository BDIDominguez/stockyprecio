package com.stock.backend.impuesto.mapper;

import com.stock.backend.impuesto.dto.ImpuestoDTO;
import com.stock.backend.impuesto.entity.Impuesto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImpuestoMapper {
    ImpuestoDTO toDto(Impuesto impuesto);
    Impuesto toEntidad(ImpuestoDTO dto);
}
