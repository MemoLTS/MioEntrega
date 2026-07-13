package com.caso3.catalogo.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.caso3.catalogo.dto.ProductoDTO;

@FeignClient(
    name = "inventario",
    url = "http://localhost:8083"
)
public interface ProductClient {

    @GetMapping("/api/inventario/productos")
    List<ProductoDTO> obtenerProductos();

    @GetMapping("/api/inventario/productos/nombre/{nombre}")
    List<ProductoDTO> obtenerPorNombre(
        @PathVariable("nombre") String nombre);

        
    @GetMapping("/api/inventario/productos/ordenados/categoria/{idCategoria}")
    List<ProductoDTO> obtenerPorCategoria(@PathVariable("idCategoria") Long idCategoria);
}