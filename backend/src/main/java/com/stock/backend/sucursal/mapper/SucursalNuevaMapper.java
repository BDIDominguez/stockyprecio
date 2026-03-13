package com.stock.backend.sucursal.mapper;

import com.stock.backend.sucursal.dto.SucursalDTO;
import com.stock.backend.sucursal.dto.SucursalNuevaDTO;
import com.stock.backend.sucursal.entity.Sucursal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SucursalNuevaMapper {
    SucursalNuevaDTO toDto(Sucursal sucursal);
    Sucursal toEntity(SucursalNuevaDTO sucursalDTO);
}
