package com.stock.backend.repository;

import com.stock.backend.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findBySucursal(Long sucursal);

    Optional<Stock> findByCodigoAndSucursal(Long codigo, Long sucursal);
}
