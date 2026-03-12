package com.stock.backend.categoria.service;

import com.stock.backend.categoria.entity.Categoria;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoriaFacadeService {
    private final CategoriaBuscarPorNombreService categoriaBuscarPorNombre;
    private final CategoriaConsultarTodosService categoriaConsultarTodos;
    private final CategoriaCrearService categoriaCrear;
    private final CategoriaModificarService categoriaModificar;
    private final CategoriaDesactivarService categoriaEliminar;
    private final CategoriaBuscarPorIdService categoriaBuscarPorId;
    private final CategoriaBuscarPorCodigoService categoriaBuscarPorCodigo;

    public Optional<Categoria> buscarPorNombre(String nombre){
        return categoriaBuscarPorNombre.consultar(nombre);
    }
    public Page<Categoria> consultarTodos(Boolean activo, int page, int size, String sort){
        return categoriaConsultarTodos.consultar(activo, page, size, sort);
    }
    public Categoria crear(Categoria datos){
        return categoriaCrear.crear(datos);
    }
    public Categoria modificar(Categoria datos, Long codigo){
        return categoriaModificar.modificar(datos, codigo);
    }
    public Categoria desactivarPorCodigo(Long codigo){
        return categoriaEliminar.desactivarPorCodigo(codigo);
    }
    public Optional<Categoria> consultarPorId(Long categoria) {
        return categoriaBuscarPorId.buscar(categoria);
    }
    public Optional<Categoria> buscarPorCodigo(Long codigo) {
        return categoriaBuscarPorCodigo.buscarPorCodigo(codigo);
    }
    public Categoria activarPorCodigo(Long codigo) {
        return categoriaEliminar.activarPorCodigo(codigo);
    }
    public Page<Categoria> buscarPorNombreIgnoreCase(String nombre, int page, int size, String sort) {
        return categoriaBuscarPorNombre.buscarIgnoreCase(nombre, page, size, sort);
    }
    public Long siguienteCodigo(){
        return categoriaConsultarTodos.siguienteCodigo();
    }
}
