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

    /**
     * Solo productos habilitados (activo = true). Útil para vitrinas donde no
     * se quiere mostrar nada deshabilitado.
     */
    public List<ProductoDTO> verCatalogoDisponible() {
        return productClient.obtenerProductos()
                .stream()
                .filter(ProductoDTO::isActivo)
                .toList();
    }

    public List<ProductoDTO> obtenerPorCategoria(Long idCategoria) {
        return productClient.obtenerPorCategoria(idCategoria);
    }

    public List<ProductoDTO> obtenerPorNombre(String nombre){
        return productClient.obtenerPorNombre(nombre);
    }

}