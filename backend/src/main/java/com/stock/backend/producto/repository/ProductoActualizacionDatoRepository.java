package com.stock.backend.producto.repository;

import com.stock.backend.producto.entity.ProductoActualizacionDato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoActualizacionDatoRepository extends JpaRepository<ProductoActualizacionDato, Long> {
    Optional<ProductoActualizacionDato> findByActualizacion(Long actualizacion);
}
