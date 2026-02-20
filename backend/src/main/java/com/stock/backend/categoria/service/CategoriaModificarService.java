package com.stock.backend.categoria.service;

import com.stock.backend.categoria.entity.Categoria;
import com.stock.backend.common.exception.RecursoNoEncontradoException;
import com.stock.backend.categoria.repository.CategoriaRespository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoriaModificarService {
    private static CategoriaRespository respository;

    public Categoria modificar(Categoria datos, Long id){
        Categoria existe = respository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe producto con ID: " + datos.getId()));
        existe.actualizar(datos);
        return respository.save(existe);
    }
}
