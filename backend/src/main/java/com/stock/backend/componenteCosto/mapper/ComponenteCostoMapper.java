package com.stock.backend.componenteCosto.mapper;

import com.stock.backend.componenteCosto.dto.ComponenteCostoDTO;
import com.stock.backend.componenteCosto.entity.ComponenteCosto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ComponenteCostoMapper {
    ComponenteCostoDTO toDto(ComponenteCosto componenteCosto);
    ComponenteCosto toEntidad(ComponenteCostoDTO dto);
}
