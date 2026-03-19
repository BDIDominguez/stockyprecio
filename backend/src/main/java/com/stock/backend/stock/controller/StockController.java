package com.stock.backend.stock.controller;

import com.stock.backend.stock.dto.StockDTO;
import com.stock.backend.stock.mapper.StockMapper;
import com.stock.backend.stock.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stock")
@AllArgsConstructor
@Validated
public class StockController {


    private final StockService service;
    private final StockMapper mapper;

    @Operation(
            summary = "Consultar stock por codigo y sucursal",
            description = "Devuelve el stock puntual de un producto en una sucursal. " +
                    "El stock se identifica funcionalmente por la combinacion codigo + sucursal."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock encontrado"),
            @ApiResponse(responseCode = "404", description = "Stock no encontrado"),
            @ApiResponse(responseCode = "400", description = "Parametros invalidos")
    })
    @GetMapping("")
    public ResponseEntity<StockDTO> consultarCodigoSucursal(
            @RequestParam @Positive Long codigo,
            @RequestParam @Positive Long sucursal) {
        StockDTO respuesta = mapper.toDto(service.consultarExistente(codigo, sucursal));
        return ResponseEntity.ok(respuesta);
    }

}
