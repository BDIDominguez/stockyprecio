package com.stock.backend.categoria.service;

import com.stock.backend.categoria.entity.Categoria;
import com.stock.backend.categoria.repository.CategoriaRespository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoriaBuscarPorIdService {
    private final CategoriaRespository respository;

    public Optional<Categoria> buscar(Long id){
        return respository.findByCodigo(id);
    }
}
