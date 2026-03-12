package com.stock.backend.sucursal.service;

import com.stock.backend.sucursal.dto.SucursalDTO;
import com.stock.backend.sucursal.entity.Sucursal;
import com.stock.backend.sucursal.repository.SucursalRepository;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SucursalService {

    private final SucursalRepository repository;

    public Page<Sucursal> consultarTodas(Boolean activo, Integer page, Integer size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());

        return repository.findByActivo(activo, pageable);
    }

    public Optional<Sucursal> consultarPorSucursal(Long sid) {
        return repository.findByCodigo(sid);
    }


    public Page<Sucursal> consultarPorNombreIgnoreCase(@Size(min = 2, message = "debe ingresar al menos 2 caracteres") String nombre, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        return repository.findByNombreContainingIgnoreCase(nombre, pageable);
    }
}
