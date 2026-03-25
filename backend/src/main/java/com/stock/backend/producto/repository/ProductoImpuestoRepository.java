package com.stock.backend.producto.repository;

import com.stock.backend.producto.entity.ProductoImpuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoImpuestoRepository extends JpaRepository<ProductoImpuesto, Long> {
    List<ProductoImpuesto> findAllByCodigoProducto(Long codigoProducto);
    List<ProductoImpuesto> findAllByCodigoProductoAndActivoTrueOrderByOrdenAplicacionAsc(Long codigoProducto);
    Optional<ProductoImpuesto> findByCodigoProductoAndImpuesto(Long codigoProducto, Long impuesto);
}
