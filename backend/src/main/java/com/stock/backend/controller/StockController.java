package com.stock.backend.controller;

import com.stock.backend.dto.StockDTO;
import com.stock.backend.entity.Stock;
import com.stock.backend.mapper.StockMapper;
import com.stock.backend.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    @Autowired
    private StockService service;

    @GetMapping("")
    public ResponseEntity<List<StockDTO>> listarStock(){
        return ResponseEntity.ok(service.consultarTodos().stream().map(StockMapper::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockDTO> consultarPorId(@PathVariable Long id){
        Optional<Stock> respuesta = service.consultarPorId(id);
        if (respuesta.isPresent()){
            return ResponseEntity.ok(StockMapper.toDto(respuesta.get()));
        }
        return ResponseEntity.notFound().build();
    }

}
