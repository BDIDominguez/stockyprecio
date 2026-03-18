package com.stock.backend.proveedor.mapper;

import com.stock.backend.proveedor.dto.ProveedorDTO;
import com.stock.backend.proveedor.entity.Proveedor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProveedorMapper {
    ProveedorDTO toDto(Proveedor proveedor);
    Proveedor toEntity(ProveedorDTO proveedorDTO);
}
