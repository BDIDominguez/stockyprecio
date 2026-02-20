package com.stock.backend.categoria.service;

import com.stock.backend.categoria.entity.Categoria;
import com.stock.backend.common.exception.RecursoNoEncontradoException;
import com.stock.backend.categoria.repository.CategoriaRespository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoriaEliminarService {
    private final CategoriaRespository respository;
    public Categoria eliminar(Long id){
        Categoria existe = respository.findById(id).orElseThrow(()-> new RecursoNoEncontradoException("No existe una Categoria con ese ID: " + id));
        existe.setActivo(false);
        return respository.save(existe);
    }
}
