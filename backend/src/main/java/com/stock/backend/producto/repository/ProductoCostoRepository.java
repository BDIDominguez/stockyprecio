package com.stock.backend.producto.repository;

import com.stock.backend.producto.entity.ProductoCosto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoCostoRepository extends JpaRepository<ProductoCosto, Long> {
    Optional<ProductoCosto> findByProducto(Long producto);
    Optional<ProductoCosto> findByProductoAndActivoTrue(Long producto);
    List<ProductoCosto> findAllByActivoTrue();
    List<ProductoCosto> findAllByActivoFalse();
}
