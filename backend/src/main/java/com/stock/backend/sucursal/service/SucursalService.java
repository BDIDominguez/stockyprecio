package com.stock.backend.sucursal.service;

import com.stock.backend.common.exception.RecursoNoEncontradoException;
import com.stock.backend.sucursal.dto.SucursalDTO;
import com.stock.backend.sucursal.dto.SucursalNuevaDTO;
import com.stock.backend.sucursal.entity.Sucursal;
import com.stock.backend.sucursal.mapper.SucursalMapper;
import com.stock.backend.sucursal.mapper.SucursalNuevaMapper;
import com.stock.backend.sucursal.repository.SucursalRepository;
import jakarta.validation.Valid;
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
    private final SucursalMapper mapper;
    private final SucursalNuevaMapper nuevaMapper;

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

    public Optional<Sucursal> consultarPorCodigo(long codigo) {
        return repository.findByCodigo(codigo);
    }

    public void desactivarPorCodigo(Long codigo) {
        Sucursal existe = repository.findByCodigo(codigo).orElseThrow(()-> new RecursoNoEncontradoException("No existe una Sucursal con el codigo " + codigo));
        existe.setActivo(false);
        repository.save(existe);
    }

    public void activarPorCodigo(Long codigo) {
        Sucursal existe = repository.findByCodigo(codigo).orElseThrow(()-> new RecursoNoEncontradoException("No existe una Sucursal con el codigo " + codigo));
        existe.setActivo(true);
        repository.save(existe);
    }

    public Sucursal crearSucursal(@Valid SucursalNuevaDTO dto) {
        Sucursal sucursal = repository.save(nuevaMapper.toEntity(dto));
        return sucursal;
    }
}
