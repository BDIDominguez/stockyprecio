package com.stock.backend.listaPrecio.repository;

import com.stock.backend.listaPrecio.entity.ListaPrecio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ListaPrecioRepository extends JpaRepository<ListaPrecio, Long> {
    Optional<ListaPrecio> findByCodigo(String codigo);
    List<ListaPrecio> findAllByActivoTrue();
    List<ListaPrecio> findAllByActivoFalse();
}
