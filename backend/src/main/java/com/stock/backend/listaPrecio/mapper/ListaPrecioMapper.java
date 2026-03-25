package com.stock.backend.listaPrecio.mapper;

import com.stock.backend.listaPrecio.dto.ListaPrecioDTO;
import com.stock.backend.listaPrecio.entity.ListaPrecio;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ListaPrecioMapper {
    ListaPrecioDTO toDto(ListaPrecio listaPrecio);
    ListaPrecio toEntidad(ListaPrecioDTO dto);
}
