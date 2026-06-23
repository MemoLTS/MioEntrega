package com.caso3.catalogo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.caso3.catalogo.dto.ProductoDTO;
import com.caso3.catalogo.service.CatalogService;

import java.util.List;

@RestController
@RequestMapping("/catalogo")
public class CatalogController {

    @Autowired
    private CatalogService service;

    @GetMapping("/PorCategoria/{categoria}")
    public ResponseEntity<List<ProductoDTO>> porCategoria(
            @PathVariable String categoria){

        return ResponseEntity.ok(
                service.obtenerPorCategoria(categoria));
    }

    @GetMapping("/ver")
    public List<ProductoDTO> verCatalogo() {
        return service.verCatalogo();
    }

    @GetMapping("/PorNombre/{nombre}")
    public ResponseEntity<Object> porNombre(
            @PathVariable String nombre){

        return ResponseEntity.ok(
                service.obtenerPorNombre(nombre));
    }

}