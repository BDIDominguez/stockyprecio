package com.stock.backend.producto.repository;

import com.stock.backend.producto.entity.ProductoActualizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductoActualizacionRepository extends JpaRepository<ProductoActualizacion, Long> {
    List<ProductoActualizacion> findAllByCodigoProducto(Long codigoProducto);
    List<ProductoActualizacion> findAllByCodigoProductoOrderByFechaCreacionDesc(Long codigoProducto);
    List<ProductoActualizacion> findAllByEstado(String estado);
    List<ProductoActualizacion> findAllByCodigoProductoAndEstado(Long codigoProducto, String estado);
    List<ProductoActualizacion> findAllByEsProgramadaTrueAndEstadoAndFechaProgramadaLessThanEqual(
            String estado,
            LocalDateTime fechaProgramada
    );
}
