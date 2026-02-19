package com.stock.backend.service.casodeuso;

import com.stock.backend.dto.ProductoCompletoDTO;
import com.stock.backend.entity.Categoria;
import com.stock.backend.entity.Producto;
import com.stock.backend.entity.Proveedor;
import com.stock.backend.entity.Stock;
import com.stock.backend.mapper.ProductoCompletoMapper;
import com.stock.backend.service.ProveedorService;
import com.stock.backend.service.categoria.CategoriaFacadeService;
import com.stock.backend.service.producto.ProductoFacadeService;
import com.stock.backend.service.stock.StockFacadeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CrearProductoCompleto {
    private final ProductoFacadeService productoService;
    private final StockFacadeService stockService;
    private final CategoriaFacadeService categoriaService;
    private final ProveedorService proveedorService;

    public ProductoCompletoDTO crear(Producto producto, Long sucursal){
        Categoria categoria = null;
        if (producto.getCategoria() != null) {
            categoria = categoriaService.consultarPorId(producto.getCategoria()).orElse(null);
            if (categoria == null) {
                producto.setCategoria(null);
            }
        }
        Proveedor proveedor =  null;
        if (producto.getProveedor() != null){
            proveedor = proveedorService.consultarPorID(producto.getProveedor()).orElse(null);
            if (proveedor == null){
                producto.setProveedor(null);
            }
        }
        Stock stock = stockService.guardar(new Stock(null,producto.getCodigo(), sucursal, 0.00, 0.00));
        producto = productoService.crear(producto);
        return ProductoCompletoMapper.toDto(producto,categoria,proveedor,stock);
    }
}
