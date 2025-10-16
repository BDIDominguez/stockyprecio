package com.stock.backend.service;

import com.stock.backend.entity.Categoria;
import com.stock.backend.repository.CategoriaRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRespository categoriaRespository;

    public List<Categoria> consultaTodos() {
        return categoriaRespository.findAll();
    }

    public Categoria consultarPorNombre(String nombre) {
        return categoriaRespository.findByNombre(nombre);

    }

    public Categoria crearCategoria(Categoria categoria) {
        return categoriaRespository.save(categoria);
    }

    public Categoria modificarCategoria(Categoria categoria) {
        Categoria respuesta = categoriaRespository.save(categoria);
        return respuesta;
    }

    public Optional<Categoria> consultarPorId(Long id) {
        return categoriaRespository.findById(id);
    }

    public Categoria buscarPorCodigo(Long codigo) {
        return categoriaRespository.findByCodigo(codigo);
    }
}
