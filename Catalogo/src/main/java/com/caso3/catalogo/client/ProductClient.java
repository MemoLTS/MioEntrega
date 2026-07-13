package com.caso3.catalogo.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.caso3.catalogo.dto.ProductoDTO;


@FeignClient(
    name = "inventario",
    url = "http://localhost:8090"
)
public interface ProductClient {

    @GetMapping("/api/v1/inventario/prods")
    List<ProductoDTO> obtenerProductos();
}
