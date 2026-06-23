package com.pruebas.unitarias.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.caso3.catalogo.dto.CategoriaDTO;

class CategoriaDTOTest {

    @Test
    void testCantidadCategorias() {
        CategoriaDTO[] categorias = CategoriaDTO.values();

        assertEquals(9, categorias.length);
    }

    @Test
    void testValueOfAlimentos() {
        CategoriaDTO categoria = CategoriaDTO.valueOf("ALIMENTOS");

        assertEquals(CategoriaDTO.ALIMENTOS, categoria);
    }

    @Test
    void testValueOfDulces() {
        CategoriaDTO categoria = CategoriaDTO.valueOf("DULCES");

        assertEquals(CategoriaDTO.DULCES, categoria);
    }

    @Test
    void testTodasLasCategoriasExisten() {
        assertNotNull(CategoriaDTO.ALIMENTOS);
        assertNotNull(CategoriaDTO.DULCES);
        assertNotNull(CategoriaDTO.BEBIDAS);
        assertNotNull(CategoriaDTO.LIMPIEZA);
        assertNotNull(CategoriaDTO.CUIDADO_PERSONAL);
        assertNotNull(CategoriaDTO.ROPA);
        assertNotNull(CategoriaDTO.HOGAR);
        assertNotNull(CategoriaDTO.ELECTRODOMESTICOS);
        assertNotNull(CategoriaDTO.OTROS);
    }

    @Test
    void testValueOfInvalido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> CategoriaDTO.valueOf("CATEGORIA_INEXISTENTE")
        );
    }
}