package com.stock.backend.categoria.service;

import com.stock.backend.categoria.entity.Categoria;
import com.stock.backend.categoria.repository.CategoriaRespository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoriaConsultarTodosActivosService {
    private final CategoriaRespository respository;

    public List<Categoria> consultar(){
        return respository.findAllByActivoTrue();
    }
}
