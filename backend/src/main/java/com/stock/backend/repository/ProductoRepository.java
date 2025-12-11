package com.stock.backend.repository;

import com.stock.backend.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findByCodigo(Long codigo);

    List<Producto> findAllByActivoTrue();

    List<Producto> findAllByActivoFalse();
}
