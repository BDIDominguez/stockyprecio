package com.stock.backend.application.crearProductoCompleto.service;

import com.stock.backend.categoria.entity.Categoria;
import com.stock.backend.categoria.service.CategoriaService;
import com.stock.backend.producto.dto.ProductoCompletoDTO;
import com.stock.backend.producto.entity.Producto;
import com.stock.backend.producto.mapper.ProductoCompletoMapper;
import com.stock.backend.producto.service.ProductoFacadeService;
import com.stock.backend.proveedor.entity.Proveedor;
import com.stock.backend.proveedor.service.ProveedorService;
import com.stock.backend.stock.entity.Stock;
import com.stock.backend.stock.service.StockService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CrearProductoCompleto {
    private final ProductoFacadeService productoService;
    private final StockService stockService;
    private final CategoriaService categoriaService;
    private final ProveedorService proveedorService;
    private final ProductoCompletoMapper productoCompletoMapper;

    @Transactional
    public ProductoCompletoDTO crear(Producto producto, Long sucursal) {
        Categoria categoria = null;
        if (producto.getCategoria() != null) {
            categoria = categoriaService.buscarPorCodigo(producto.getCategoria()).orElse(null);
            if (categoria == null) {
                producto.setCategoria(null);
            }
        }

        Proveedor proveedor = null;
        if (producto.getProveedor() != null) {
            proveedor = proveedorService.buscarPorCodigo(producto.getProveedor()).orElse(null);
            if (proveedor == null) {
                producto.setProveedor(null);
            }
        }

        producto = productoService.crear(producto);
        Stock stock = stockService.crearInicial(producto.getCodigo(), sucursal);
        return productoCompletoMapper.toDto(producto, categoria, proveedor, stock);
    }
}
