package com.stock.backend.service.categoria;

import com.stock.backend.entity.Categoria;
import com.stock.backend.exception.RecursoNoEncontradoException;
import com.stock.backend.repository.CategoriaRespository;
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
