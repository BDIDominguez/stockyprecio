package com.stock.backend.componenteCosto.mapper;

import com.stock.backend.componenteCosto.dto.ComponenteCostoNuevoDTO;
import com.stock.backend.componenteCosto.entity.ComponenteCosto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ComponenteCostoNuevoMapper {
    ComponenteCostoNuevoDTO toDto(ComponenteCosto componenteCosto);
    ComponenteCosto toEntidad(ComponenteCostoNuevoDTO dto);
}
