package com.stock.backend.producto.repository;

import com.stock.backend.producto.entity.ProductoPrecio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoPrecioRepository extends JpaRepository<ProductoPrecio, Long> {
    List<ProductoPrecio> findAllByCodigoProducto(Long codigoProducto);
    List<ProductoPrecio> findAllByListaPrecio(Long listaPrecio);
    List<ProductoPrecio> findAllByCodigoProductoAndActivoTrue(Long codigoProducto);
    Optional<ProductoPrecio> findByCodigoProductoAndListaPrecio(Long codigoProducto, Long listaPrecio);
    Optional<ProductoPrecio> findByCodigoProductoAndListaPrecioAndActivoTrue(Long codigoProducto, Long listaPrecio);
}
