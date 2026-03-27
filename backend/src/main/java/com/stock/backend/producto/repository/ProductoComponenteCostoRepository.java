package com.stock.backend.producto.repository;

import com.stock.backend.producto.entity.ProductoComponenteCosto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoComponenteCostoRepository extends JpaRepository<ProductoComponenteCosto, Long> {
    List<ProductoComponenteCosto> findAllByProducto(Long producto);
    List<ProductoComponenteCosto> findAllByProductoAndActivoTrue(Long producto);
    Optional<ProductoComponenteCosto> findByProductoAndComponente(Long producto, Long componente);
    boolean existsByComponente(Long componente);
    void deleteAllByProducto(Long producto);
}
