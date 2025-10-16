package com.stock.backend.repository;

import com.stock.backend.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRespository extends JpaRepository<Categoria, Long> {
    Categoria findByNombre(String nombre);
    Categoria findByCodigo(Long codigo);
}
