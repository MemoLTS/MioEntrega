package com.pruebas.unitarias.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;



import java.util.ArrayList;
import java.util.List;

import com.caso3.catalogo.controller.CatalogController;
import com.caso3.catalogo.dto.ProductoDTO;
import com.caso3.catalogo.service.CatalogService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = CatalogController.class)
@AutoConfigureMockMvc
class CatalogControllerTest {

        @SuppressWarnings("unused")
        @Autowired
        private MockMvc mockMvc;

        @InjectMocks
        private CatalogController controller;

        @MockBean
        private CatalogService service;

        private ProductoDTO producto;

        @BeforeEach
        void setUp() {
                producto = new ProductoDTO();
                producto.setId(1L);
                producto.setNombre("Coca Cola");
        }

        @Test
        void testVerCatalogo() {

                List<ProductoDTO> lista = new ArrayList<>();
                lista.add(producto);

                when(service.verCatalogo()).thenReturn(lista);

                List<ProductoDTO> resultado = controller.verCatalogo();

                assertNotNull(resultado);
                assertEquals(1, resultado.size());

                verify(service).verCatalogo();
        }

        @Test
        void testVerCatalogoError() {

                when(service.verCatalogo())
                        .thenThrow(new RuntimeException("Error catálogo"));

                RuntimeException ex = assertThrows(
                        RuntimeException.class,
                        () -> controller.verCatalogo());

                assertEquals("Error catálogo", ex.getMessage());

                verify(service).verCatalogo();
        }

        @Test
        void testPorCategoria() {

                List<ProductoDTO> lista = new ArrayList<>();
                lista.add(producto);

                when(service.obtenerPorCategoria("BEBIDAS"))
                        .thenReturn(lista);

                ResponseEntity<List<ProductoDTO>> response =
                        controller.porCategoria("BEBIDAS");

                assertEquals(200, response.getStatusCode().value());
                assertEquals(lista, response.getBody());

                verify(service).obtenerPorCategoria("BEBIDAS");
        }

        @Test
        void testPorCategoriaError() {

                when(service.obtenerPorCategoria("BEBIDAS"))
                        .thenThrow(new RuntimeException("Error categoría"));

                RuntimeException ex = assertThrows(
                        RuntimeException.class,
                        () -> controller.porCategoria("BEBIDAS"));

                assertEquals("Error categoría", ex.getMessage());

                verify(service).obtenerPorCategoria("BEBIDAS");
        }

        @Test
        void testPorNombre() {

                List<ProductoDTO> lista = new ArrayList<>();
                lista.add(producto);

                when(service.obtenerPorNombre("Coca Cola"))
                        .thenReturn(lista);

                ResponseEntity<Object> response =
                        controller.porNombre("Coca Cola");

                assertEquals(200, response.getStatusCode().value());
                assertEquals(lista, response.getBody());

                verify(service).obtenerPorNombre("Coca Cola");
        }

        @Test
        void testPorNombreError() {

                when(service.obtenerPorNombre("Coca Cola"))
                        .thenThrow(new RuntimeException("Error nombre"));

                RuntimeException ex = assertThrows(
                        RuntimeException.class,
                        () -> controller.porNombre("Coca Cola"));

                assertEquals("Error nombre", ex.getMessage());

                verify(service).obtenerPorNombre("Coca Cola");
        }
}