package com.stock.backend.impuesto.mapper;

import com.stock.backend.impuesto.dto.ImpuestoNuevoDTO;
import com.stock.backend.impuesto.entity.Impuesto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImpuestoNuevoMapper {
    ImpuestoNuevoDTO toDto(Impuesto impuesto);
    Impuesto toEntidad(ImpuestoNuevoDTO dto);
}
