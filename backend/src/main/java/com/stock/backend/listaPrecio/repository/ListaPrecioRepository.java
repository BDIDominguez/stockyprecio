package com.stock.backend.listaPrecio.repository;

import com.stock.backend.listaPrecio.entity.ListaPrecio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ListaPrecioRepository extends JpaRepository<ListaPrecio, Long> {
    Optional<ListaPrecio> findByCodigo(Long codigo);
    Page<ListaPrecio> findByActivo(Boolean activo, Pageable pageable);
    Page<ListaPrecio> findByNombreContainingIgnoreCaseAndActivo(String nombre, Boolean activo, Pageable pageable);
    Page<ListaPrecio> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
    ListaPrecio findTopByOrderByCodigoDesc();
}
