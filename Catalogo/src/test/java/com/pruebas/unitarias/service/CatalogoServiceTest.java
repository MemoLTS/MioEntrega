package com.pruebas.unitarias.service;

import com.caso3.catalogo.client.ProductClient;
import com.caso3.catalogo.dto.ProductoDTO;
import com.caso3.catalogo.service.CatalogService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatalogoServiceTest {
        @Mock
        private ProductClient productClient;

        @InjectMocks
        private CatalogService catalogService;

        private ProductoDTO producto;

        @BeforeEach
        void setUp() {
                producto = new ProductoDTO();
                producto.setId(1L);
                producto.setNombre("Coca Cola");
        }

        @Test
        void testVerCatalogo() {

        List<ProductoDTO> productos = List.of(producto);

        when(productClient.obtenerProductos())
                .thenReturn(productos);

        List<ProductoDTO> resultado = catalogService.verCatalogo();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());

        verify(productClient, times(1))
                .obtenerProductos();
        }

        @Test
        void testVerCatalogoError() {

                when(productClient.obtenerProductos())
                        .thenThrow(new RuntimeException("Error inventario"));

                RuntimeException ex = assertThrows(
                        RuntimeException.class,
                        () -> catalogService.verCatalogo());

                assertEquals("Error inventario", ex.getMessage());

                verify(productClient, times(1))
                        .obtenerProductos();
        }

        @Test
        void testObtenerPorCategoria() {

                Long idCategoria = 1L;
                List<ProductoDTO> productos = List.of(producto);

                when(productClient.obtenerProductos())
                        .thenReturn(productos);

                List<ProductoDTO> resultado =
                        catalogService.obtenerPorCategoria(idCategoria);

                assertNotNull(resultado);
                assertEquals(1, resultado.size());
                assertEquals(productos, resultado);

                verify(productClient, times(1))
                        .obtenerProductos();
        }

        @Test
        void testObtenerPorCategoriaError() {

                Long idCategoria = 1L;

                when(productClient.obtenerProductos())
                        .thenThrow(new RuntimeException("Error categoría"));

                RuntimeException ex = assertThrows(
                        RuntimeException.class,
                        () -> catalogService.obtenerPorCategoria(idCategoria));

                assertEquals("Error categoría", ex.getMessage());

                verify(productClient, times(1))
                        .obtenerProductos();
        }

        @Test
        void testVerCatalogoDisponibleFiltraInactivos() {

                ProductoDTO activo = new ProductoDTO();
                activo.setId(1L);
                activo.setNombre("Coca Cola");
                activo.setActivo(true);

                ProductoDTO inactivo = new ProductoDTO();
                inactivo.setId(2L);
                inactivo.setNombre("Producto descontinuado");
                inactivo.setActivo(false);

                when(productClient.obtenerProductos())
                        .thenReturn(List.of(activo, inactivo));

                List<ProductoDTO> resultado = catalogService.verCatalogoDisponible();

                assertNotNull(resultado);
                assertEquals(1, resultado.size());
                assertEquals("Coca Cola", resultado.get(0).getNombre());

                verify(productClient, times(1))
                        .obtenerProductos();
        }

        @Test
        void testObtenerPorNombre() {

                String nombre = "Coca Cola";
                List<ProductoDTO> productos = List.of(producto);

                when(productClient.obtenerProductos())
                        .thenReturn(productos);

                List<ProductoDTO> resultado =
                        catalogService.obtenerPorNombre(nombre);

                assertNotNull(resultado);
                assertEquals(1, resultado.size());
                assertEquals(productos, resultado);

                verify(productClient, times(1))
                        .obtenerProductos();
        }

        @Test
        void testObtenerPorNombreError() {

                String nombre = "Coca Cola";

                when(productClient.obtenerProductos())
                        .thenThrow(new RuntimeException("Error nombre"));

                RuntimeException ex = assertThrows(
                        RuntimeException.class,
                        () -> catalogService.obtenerPorNombre(nombre));

                assertEquals("Error nombre", ex.getMessage());

                verify(productClient, times(1))
                        .obtenerProductos();
        }
}