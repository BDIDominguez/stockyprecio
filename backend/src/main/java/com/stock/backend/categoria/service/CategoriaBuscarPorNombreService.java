package com.stock.backend.categoria.service;

import com.stock.backend.categoria.entity.Categoria;
import com.stock.backend.categoria.repository.CategoriaRespository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoriaBuscarPorNombreService {
    private final CategoriaRespository repository;

    public Optional<Categoria> consultar(String nombre){
        return repository.findByNombre(nombre);
    }

    public Page<Categoria> buscarIgnoreCase(String nombre, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        return repository.findByNombreContainingIgnoreCase(nombre, pageable);
    }
}
