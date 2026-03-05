package com.stock.backend.categoria.service;

import com.stock.backend.categoria.entity.Categoria;
import com.stock.backend.categoria.repository.CategoriaRespository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoriaConsultarTodosService {
    private final CategoriaRespository respository;

    public Page<Categoria> consultar(Boolean activo, int pagina, int tamaño, String ordenarPor){
        Pageable pageable = PageRequest.of(pagina,tamaño, Sort.by(ordenarPor).ascending());
        return respository.findByActivo(activo, pageable);
    }

}
