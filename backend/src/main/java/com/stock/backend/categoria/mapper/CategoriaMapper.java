package com.stock.backend.categoria.mapper;

import org.mapstruct.Mapper;

import com.stock.backend.categoria.dto.CategoriaDTO;
import com.stock.backend.categoria.entity.Categoria;

@Mapper(componentModel="spring")
public interface CategoriaMapper {

    CategoriaDTO toDto(Categoria cat);

    Categoria toEntidad(CategoriaDTO dto);
}
