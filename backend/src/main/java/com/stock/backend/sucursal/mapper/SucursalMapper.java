package com.stock.backend.sucursal.mapper;

import com.stock.backend.sucursal.dto.SucursalDTO;
import com.stock.backend.sucursal.entity.Sucursal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SucursalMapper {
    SucursalDTO toDto(Sucursal sucursal);
    Sucursal toEntity(SucursalDTO sucursalDTO);
}
