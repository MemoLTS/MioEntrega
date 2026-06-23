package com.caso3.catalogo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caso3.catalogo.client.ProductClient;
import com.caso3.catalogo.dto.ProductoDTO;


import java.util.List;


@Service
public class CatalogService {

    @Autowired
    private ProductClient productClient;

    public CatalogService(ProductClient productClient) {
        this.productClient = productClient;
    }

    public List<ProductoDTO> verCatalogo() {
        return productClient.obtenerProductos();
    }
    public List<ProductoDTO> obtenerPorCategoria(String categoria) {
        return productClient.obtenerPorCategoria(categoria);
    }

    public List<ProductoDTO> obtenerPorNombre(String nombre){
        return productClient.obtenerPorNombre(nombre);
    }

}