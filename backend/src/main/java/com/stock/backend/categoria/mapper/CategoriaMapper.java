package com.stock.backend.categoria.mapper;

import com.stock.backend.categoria.dto.CategoriaDTO;
import com.stock.backend.categoria.entity.Categoria;

public class CategoriaMapper {

    public static CategoriaDTO toDto(Categoria cat){
        return new CategoriaDTO(
                cat.getId(),
                cat.getCodigo(),
                cat.getNombre(),
                cat.getDescripcion(),
                cat.getActivo(),
                cat.getFechaCreacion(),
                cat.getFechaModificacion()
        );
    }

    public static Categoria toEntidad(CategoriaDTO dto){
        return new Categoria(dto.id(), dto.codigo(), dto.nombre(), dto.descripcion(), dto.activo(), dto.fechaCreacion(), dto.fechaModificacion() );
    }
}
