package com.stock.backend.producto.repository;

import com.stock.backend.producto.entity.ProductoActualizacionCosto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoActualizacionCostoRepository extends JpaRepository<ProductoActualizacionCosto, Long> {
    Optional<ProductoActualizacionCosto> findByActualizacion(Long actualizacion);
}
