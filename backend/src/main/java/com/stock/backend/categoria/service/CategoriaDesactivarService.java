package com.stock.backend.categoria.service;

import com.stock.backend.categoria.entity.Categoria;
import com.stock.backend.common.exception.RecursoNoEncontradoException;
import com.stock.backend.categoria.repository.CategoriaRespository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoriaDesactivarService {
    private final CategoriaRespository respository;
    public Categoria desactivarPorCodigo(Long codigo){
        Categoria existe = respository.findByCodigo(codigo).orElseThrow(()-> new RecursoNoEncontradoException("No existe una Categoria con ese Codigo: " + codigo));
        existe.setActivo(false);
        return respository.save(existe);
    }
}
