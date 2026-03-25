package com.stock.backend.listaPrecio.mapper;

import com.stock.backend.listaPrecio.dto.ListaPrecioNuevaDTO;
import com.stock.backend.listaPrecio.entity.ListaPrecio;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ListaPrecioNuevaMapper {
    ListaPrecioNuevaDTO toDto(ListaPrecio listaPrecio);
    ListaPrecio toEntidad(ListaPrecioNuevaDTO dto);
}
