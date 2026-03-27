package com.stock.backend.producto.repository;

import com.stock.backend.producto.entity.ProductoCostoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoCostoDetalleRepository extends JpaRepository<ProductoCostoDetalle, Long> {
    List<ProductoCostoDetalle> findAllByProductoOrderByEtapaAplicacionAscComponenteAsc(Long producto);
    void deleteAllByProducto(Long producto);
}
