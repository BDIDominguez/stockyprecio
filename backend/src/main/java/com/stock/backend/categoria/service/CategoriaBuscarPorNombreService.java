package com.stock.backend.categoria.service;

import com.stock.backend.categoria.entity.Categoria;
import com.stock.backend.categoria.repository.CategoriaRespository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoriaBuscarPorNombreService {
    private static CategoriaRespository repository;

    public Optional<Categoria> consultar(String nombre){
        return repository.findByNombre(nombre);
    }
}
