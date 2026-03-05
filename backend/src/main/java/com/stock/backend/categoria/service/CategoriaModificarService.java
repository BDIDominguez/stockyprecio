package com.stock.backend.categoria.service;

import com.stock.backend.categoria.entity.Categoria;
import com.stock.backend.common.exception.RecursoNoEncontradoException;
import com.stock.backend.categoria.repository.CategoriaRespository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoriaModificarService {
    private final CategoriaRespository respository;

    public Categoria modificar(Categoria datos, Long codigo){
        Categoria existe = respository.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe producto con Codigo: " + datos.getCodigo()));
        existe.actualizar(datos);
        return respository.save(existe);
    }
}
