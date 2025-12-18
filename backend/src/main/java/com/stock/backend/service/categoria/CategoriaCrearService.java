package com.stock.backend.service.categoria;

import com.stock.backend.entity.Categoria;
import com.stock.backend.exception.CodigoRepetidoException;
import com.stock.backend.repository.CategoriaRespository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoriaCrearService {
    private static CategoriaRespository respository;

    public Categoria crear(Categoria datos){
        respository.findByCodigo(datos.getCodigo()).ifPresent(c -> {
            throw new CodigoRepetidoException("Ya existe ese codigo en la tabla " + datos.getCodigo());
        });
        return respository.save(datos);
    }
}
