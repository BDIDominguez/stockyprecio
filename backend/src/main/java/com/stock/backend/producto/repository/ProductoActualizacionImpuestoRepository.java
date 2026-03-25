package com.stock.backend.producto.repository;

import com.stock.backend.producto.entity.ProductoActualizacionImpuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoActualizacionImpuestoRepository extends JpaRepository<ProductoActualizacionImpuesto, Long> {
    List<ProductoActualizacionImpuesto> findAllByActualizacionOrderByOrdenAplicacionAsc(Long actualizacion);
    Optional<ProductoActualizacionImpuesto> findByActualizacionAndImpuesto(Long actualizacion, Long impuesto);
}
