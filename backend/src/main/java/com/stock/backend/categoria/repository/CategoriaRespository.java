package com.stock.backend.categoria.repository;

import com.stock.backend.categoria.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRespository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNombre(String nombre);
    Optional<Categoria> findByCodigo(Long codigo);
    List<Categoria> findAllByActivoTrue();
    List<Categoria> findAllByActivoFalse();
}
