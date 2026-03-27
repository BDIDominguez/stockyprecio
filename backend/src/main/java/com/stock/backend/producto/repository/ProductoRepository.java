package com.stock.backend.producto.repository;

import com.stock.backend.producto.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findByCodigo(Long codigo);
    Optional<Producto> findByCodigoAndActivo(Long codigo, Boolean activo);

    List<Producto> findAllByActivoTrue();

    List<Producto> findAllByActivoFalse();

    Page<Producto> findByActivo(Boolean activo, Pageable pageable);

    Page<Producto> findByNombreContainingIgnoreCaseAndActivo(String nombre, Boolean activo, Pageable pageable);

    @Query("""
            select p
            from Producto p
            where (:activo is null or p.activo = :activo)
              and str(p.codigo) like concat('%', :codigo, '%')
            """)
    Page<Producto> findByCodigoContainingAndActivo(
            @Param("codigo") String codigo,
            @Param("activo") Boolean activo,
            Pageable pageable
    );

    Producto findTopByOrderByCodigoDesc();
}
