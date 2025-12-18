package com.stock.backend.service.categoria;

import com.stock.backend.entity.Categoria;
import com.stock.backend.repository.CategoriaRespository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoriaBuscarPorIdService {
    private final CategoriaRespository respository;

    public Optional<Categoria> buscar(Long id){
        return respository.findByCodigo(id);
    }
}
