package com.stock.backend.categoria.mapper;

import org.mapstruct.Mapper;

import com.stock.backend.categoria.dto.CategoriaNuevaDTO;
import com.stock.backend.categoria.entity.Categoria;

@Mapper(componentModel = "spring")
public interface CategoriaNuevaMapper {

    Categoria toEntidad(CategoriaNuevaDTO cat);

    CategoriaNuevaDTO toDto(Categoria dto);

}
