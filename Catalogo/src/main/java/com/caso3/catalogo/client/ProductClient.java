package com.caso3.catalogo.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.caso3.catalogo.dto.ProductoDTO;

/**
 * Cliente Feign hacia Inventario (fuente de verdad de los productos).
 * URL y ruta corregidas para apuntar al puerto y prefijo reales del
 * InvController de Inventario (server.port=8090, /api/v1/inventario/**).
 * Solo se necesita traer TODOS los productos: el filtrado por categoría,
 * por nombre y por estado (activo/inactivo) se hace localmente en Catalogo
 * una vez sincronizados los datos, para evitar dos fuentes de lógica de filtrado.
 */
@FeignClient(
    name = "inventario",
    url = "http://localhost:8090"
)
public interface ProductClient {

    @GetMapping("/api/v1/inventario/prods")
    List<ProductoDTO> obtenerProductos();
}
