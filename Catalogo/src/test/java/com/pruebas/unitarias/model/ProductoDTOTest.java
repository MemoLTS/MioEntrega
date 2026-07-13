package com.pruebas.unitarias.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.caso3.catalogo.dto.CategoriaDTO;
import com.caso3.catalogo.dto.ProductoDTO;

class ProductoDTOTest {

    @Test
    void testPrecioFinalSinDescuentoEsIgualAlPrecio() {
        ProductoDTO producto = new ProductoDTO();
        producto.setPrecio(1000.0);
        producto.setDescuentoPorcentaje(0.0);

        assertEquals(1000.0, producto.getPrecioFinal());
    }

    @Test
    void testPrecioFinalConDescuentoSeCalculaCorrectamente() {
        ProductoDTO producto = new ProductoDTO();
        producto.setPrecio(1000.0);
        producto.setDescuentoPorcentaje(20.0);

        assertEquals(800.0, producto.getPrecioFinal());
    }

    @Test
    void testPrecioFinalConPrecioNuloDevuelveNulo() {
        ProductoDTO producto = new ProductoDTO();
        producto.setPrecio(null);
        producto.setDescuentoPorcentaje(15.0);

        assertNull(producto.getPrecioFinal());
    }

    @Test
    void testActivoPorDefectoEsTrue() {
        ProductoDTO producto = new ProductoDTO();
        assertTrue(producto.isActivo());
    }

    @Test
    void testSettersYGetters() {
        CategoriaDTO categoria = new CategoriaDTO(1L, "Bebidas", "Bebidas y jugos");

        ProductoDTO producto = new ProductoDTO();
        producto.setId(10L);
        producto.setNombre("Agua mineral");
        producto.setPrecio(500.0);
        producto.setStock(25);
        producto.setCategoria(categoria);
        producto.setActivo(false);
        producto.setDescuentoPorcentaje(10.0);

        assertEquals(10L, producto.getId());
        assertEquals("Agua mineral", producto.getNombre());
        assertEquals(500.0, producto.getPrecio());
        assertEquals(25, producto.getStock());
        assertEquals(categoria, producto.getCategoria());
        assertEquals(false, producto.isActivo());
        assertEquals(10.0, producto.getDescuentoPorcentaje());
        assertEquals(450.0, producto.getPrecioFinal());
    }

    @Test
    void testEqualsEntreProductosConMismosDatos() {
        CategoriaDTO categoria = new CategoriaDTO(1L, "Bebidas", "Bebidas y jugos");

        ProductoDTO p1 = new ProductoDTO(1L, "Coca Cola", 1000.0, 10, categoria, true, 0.0);
        ProductoDTO p2 = new ProductoDTO(1L, "Coca Cola", 1000.0, 10, categoria, true, 0.0);

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void testNoEqualsEntreProductosDistintos() {
        CategoriaDTO categoria = new CategoriaDTO(1L, "Bebidas", "Bebidas y jugos");

        ProductoDTO p1 = new ProductoDTO(1L, "Coca Cola", 1000.0, 10, categoria, true, 0.0);
        ProductoDTO p2 = new ProductoDTO(2L, "Sprite", 900.0, 5, categoria, true, 0.0);

        assertNotEquals(p1, p2);
    }
}
