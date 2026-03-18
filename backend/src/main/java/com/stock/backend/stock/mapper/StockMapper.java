package com.stock.backend.stock.mapper;

import com.stock.backend.stock.dto.StockDTO;
import com.stock.backend.stock.entity.Stock;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockMapper {
    StockDTO toDto(Stock origen);

    Stock toEntity(StockDTO origen);
}
