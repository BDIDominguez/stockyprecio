package com.stock.backend.categoria.mapper;

import com.stock.backend.categoria.dto.CategoriaNuevaDTO;
import com.stock.backend.categoria.entity.Categoria;

public class CategoriaNuevaMapper {

    public static Categoria toEntidad(CategoriaNuevaDTO cat){
        return new Categoria(null, cat.codigo(), cat.nombre(), cat.descripcion(), cat.activo(), cat.fechaCreacion(), cat.fechaModificacion());
    }

    public static CategoriaNuevaDTO toDTO(Categoria dto){
        return new CategoriaNuevaDTO(dto.getCodigo(), dto.getNombre(), dto.getDescripcion(), dto.getActivo(), dto.getFechaCreacion(), dto.getFechaModificacion());
    }

}
