package com.stock.backend.producto.repository;

import com.stock.backend.producto.entity.ProductoActualizacionPrecio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoActualizacionPrecioRepository extends JpaRepository<ProductoActualizacionPrecio, Long> {
    List<ProductoActualizacionPrecio> findAllByActualizacion(Long actualizacion);
    Optional<ProductoActualizacionPrecio> findByActualizacionAndListaPrecio(Long actualizacion, String listaPrecio);
}
