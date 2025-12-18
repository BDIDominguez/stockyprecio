package com.stock.backend.service.categoria;

import com.stock.backend.entity.Categoria;
import com.stock.backend.repository.CategoriaRespository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoriaConsultarTodosService {
    private static CategoriaRespository respository;

    public List<Categoria> consultar(){
        return respository.findAll();
    }

}
