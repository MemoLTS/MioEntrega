package com.caso3.catalogo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.caso3.catalogo.dto.ProductoDTO;
import com.caso3.catalogo.service.CatalogService;

import java.util.List;

@RestController
@RequestMapping("api/v1/catalogo")
public class CatalogController {

    @Autowired
    private CatalogService service;

    @GetMapping("/PorCategoria/{idCategoria}")
    public ResponseEntity<List<ProductoDTO>> porCategoria(
            @PathVariable Long idCategoria){

        return ResponseEntity.ok(
                service.obtenerPorCategoria(idCategoria));
    }

    @GetMapping("/ver")
    public List<ProductoDTO> verCatalogo() {
        return service.verCatalogo();
    }

    @GetMapping("/ver/disponibles")
    public List<ProductoDTO> verCatalogoDisponible() {
        return service.verCatalogoDisponible();
    }

    @GetMapping("/PorNombre/{nombre}")
    public ResponseEntity<Object> porNombre(
            @PathVariable String nombre){

        return ResponseEntity.ok(
                service.obtenerPorNombre(nombre));
    }

    @GetMapping("/PorNombreCategoria/{nombreCategoria}")
    public ResponseEntity<List<ProductoDTO>> porNombreCategoria(
            @PathVariable String nombreCategoria){

        return ResponseEntity.ok(
                service.obtenerPorNombreCategoria(nombreCategoria));
    }

}