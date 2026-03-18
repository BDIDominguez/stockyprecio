package com.stock.backend.categoria.repository;

import com.stock.backend.categoria.entity.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNombre(String nombre);
    Optional<Categoria> findByCodigo(Long codigo);
    Page<Categoria> findByActivo(Boolean activo, Pageable pageable);
    Page<Categoria> findByNombreContainingIgnoreCaseAndActivo(String nombre, Boolean activo, Pageable pageable);
    Page<Categoria> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
    Categoria findTopByOrderByCodigoDesc();
}
