package com.stock.backend.producto.repository;

import com.stock.backend.producto.entity.ProductoPrecio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoPrecioRepository extends JpaRepository<ProductoPrecio, Long> {
    List<ProductoPrecio> findAllByProducto(Long producto);
    List<ProductoPrecio> findAllByListaPrecio(Long listaPrecio);
    List<ProductoPrecio> findAllByProductoAndActivoTrue(Long producto);
    Optional<ProductoPrecio> findByProductoAndListaPrecio(Long producto, Long listaPrecio);
    Optional<ProductoPrecio> findByProductoAndListaPrecioAndActivoTrue(Long producto, Long listaPrecio);
}
