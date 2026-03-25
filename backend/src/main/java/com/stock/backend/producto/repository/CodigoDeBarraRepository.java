package com.stock.backend.producto.repository;

import com.stock.backend.producto.entity.CodigoDeBarra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodigoDeBarraRepository extends JpaRepository<CodigoDeBarra, Long> {
    Optional<CodigoDeBarra> findByBarra(String barra);
    List<CodigoDeBarra> findAllByCodigoProducto(Long codigoProducto);
    boolean existsByBarra(String barra);
}
