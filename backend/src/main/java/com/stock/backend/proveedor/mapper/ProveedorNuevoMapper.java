package com.stock.backend.proveedor.mapper;

import com.stock.backend.proveedor.dto.ProveedorNuevoDTO;
import com.stock.backend.proveedor.entity.Proveedor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProveedorNuevoMapper {
    ProveedorNuevoDTO toDto(Proveedor proveedor);
    Proveedor toEntity(ProveedorNuevoDTO proveedorDTO);
}
