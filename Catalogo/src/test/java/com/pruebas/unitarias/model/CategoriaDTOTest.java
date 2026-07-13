package com.pruebas.unitarias.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.caso3.catalogo.dto.CategoriaDTO;

class CategoriaDTOTest {

    @Test
    void testCrearCategoriaConDatos() {
        CategoriaDTO categoria = new CategoriaDTO(1L, "ALIMENTOS", "Productos alimenticios");

        assertEquals(1L, categoria.getId());
        assertEquals("ALIMENTOS", categoria.getNombre());
        assertEquals("Productos alimenticios", categoria.getDescripcion());
    }

    @Test
    void testSettersYGetters() {
        CategoriaDTO categoria = new CategoriaDTO();
        categoria.setId(2L);
        categoria.setNombre("BEBIDAS");
        categoria.setDescripcion("Bebidas en general");

        assertEquals(2L, categoria.getId());
        assertEquals("BEBIDAS", categoria.getNombre());
        assertEquals("Bebidas en general", categoria.getDescripcion());
    }

    @Test
    void testEqualsEntreCategoriasIguales() {
        CategoriaDTO categoria1 = new CategoriaDTO(3L, "DULCES", "Confitería");
        CategoriaDTO categoria2 = new CategoriaDTO(3L, "DULCES", "Confitería");

        assertEquals(categoria1, categoria2);
    }

    @Test
    void testNoEqualsEntreCategoriasDistintas() {
        CategoriaDTO categoria1 = new CategoriaDTO(4L, "ROPA", "Vestuario");
        CategoriaDTO categoria2 = new CategoriaDTO(5L, "HOGAR", "Artículos para el hogar");

        assertNotEquals(categoria1, categoria2);
    }
}
