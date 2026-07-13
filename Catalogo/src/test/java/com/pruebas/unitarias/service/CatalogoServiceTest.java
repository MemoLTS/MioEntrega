package com.pruebas.unitarias.service;

import com.caso3.catalogo.client.ProductClient;
import com.caso3.catalogo.dto.CategoriaDTO;
import com.caso3.catalogo.dto.ProductoDTO;
import com.caso3.catalogo.repository.CatalogRepository;
import com.caso3.catalogo.repository.CategoriaCatalogRepository;
import com.caso3.catalogo.service.CatalogService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatalogoServiceTest {

        @Mock
        private ProductClient productClient;

        @Mock
        private CatalogRepository catalogRepository;

        @Mock
        private CategoriaCatalogRepository categoriaCatalogRepository;

        private CatalogService catalogService;

        private ProductoDTO producto;

        @BeforeEach
        void setUp() {
                catalogService = new CatalogService(productClient, catalogRepository, categoriaCatalogRepository);

                producto = new ProductoDTO();
                producto.setId(1L);
                producto.setNombre("Coca Cola");
                producto.setPrecio(1000.0);
                producto.setStock(10);
                producto.setActivo(true);
        }

        private ProductoDTO copiar(ProductoDTO original) {
                ProductoDTO copia = new ProductoDTO();
                copia.setId(original.getId());
                copia.setNombre(original.getNombre());
                copia.setPrecio(original.getPrecio());
                copia.setStock(original.getStock());
                copia.setCategoria(original.getCategoria());
                copia.setActivo(original.isActivo());
                copia.setDescuentoPorcentaje(original.getDescuentoPorcentaje());
                return copia;
        }

        @Test
        void testVerCatalogo_devuelveProductosSincronizados() {

                when(productClient.obtenerProductos())
                        .thenReturn(List.of(producto));

                // Antes de sincronizar no había nada guardado localmente.
                when(catalogRepository.findAll())
                        .thenReturn(List.of())
                        .thenReturn(List.of(producto));

                List<ProductoDTO> resultado = catalogService.verCatalogo();

                assertNotNull(resultado);
                assertEquals(1, resultado.size());
                assertEquals("Coca Cola", resultado.get(0).getNombre());

                verify(productClient, times(1)).obtenerProductos();
                verify(catalogRepository, times(1)).save(producto);
        }

        @Test
        void testVerCatalogo_sobrescribeConDatosDeInventarioCuandoDifieren() {

                ProductoDTO local = copiar(producto);
                local.setNombre("Nombre desactualizado");
                local.setPrecio(500.0);

                // "producto" (el de Inventario) tiene nombre y precio distintos a "local"
                when(productClient.obtenerProductos())
                        .thenReturn(List.of(producto));

                when(catalogRepository.findAll())
                        .thenReturn(List.of(local))
                        .thenReturn(List.of(producto));

                List<ProductoDTO> resultado = catalogService.verCatalogo();

                assertEquals("Coca Cola", resultado.get(0).getNombre());
                assertEquals(1000.0, resultado.get(0).getPrecio());
                verify(catalogRepository, times(1)).save(producto);
        }

        @Test
        void testVerCatalogo_noSobrescribeCuandoLosDatosCoinciden() {

                ProductoDTO local = copiar(producto);

                when(productClient.obtenerProductos())
                        .thenReturn(List.of(producto));

                when(catalogRepository.findAll())
                        .thenReturn(List.of(local))
                        .thenReturn(List.of(local));

                catalogService.verCatalogo();

                verify(catalogRepository, never()).save(any());
        }

        @Test
        void testVerCatalogo_eliminaProductosQueYaNoExistenEnInventario() {

                ProductoDTO productoEliminado = copiar(producto);
                productoEliminado.setId(2L);
                productoEliminado.setNombre("Descontinuado");

                // Inventario ya solo devuelve el producto con id 1
                when(productClient.obtenerProductos())
                        .thenReturn(List.of(producto));

                when(catalogRepository.findAll())
                        .thenReturn(List.of(producto, productoEliminado))
                        .thenReturn(List.of(producto));

                catalogService.verCatalogo();

                @SuppressWarnings("unchecked")
                ArgumentCaptor<Iterable<Long>> captor = ArgumentCaptor.forClass(Iterable.class);
                verify(catalogRepository, times(1)).deleteAllById(captor.capture());

                List<Long> idsEliminados = new java.util.ArrayList<>();
                captor.getValue().forEach(idsEliminados::add);

                assertEquals(1, idsEliminados.size());
                assertTrue(idsEliminados.contains(2L));
        }

        @Test
        void testVerCatalogo_propagaErrorDeInventario() {

                when(productClient.obtenerProductos())
                        .thenThrow(new RuntimeException("Error inventario"));

                RuntimeException ex = assertThrows(
                        RuntimeException.class,
                        () -> catalogService.verCatalogo());

                assertEquals("Error inventario", ex.getMessage());
                verify(productClient, times(1)).obtenerProductos();
        }

        @Test
        void testVerCatalogo_filtraInactivos() {

                ProductoDTO activo = copiar(producto);

                ProductoDTO inactivo = copiar(producto);
                inactivo.setId(2L);
                inactivo.setNombre("Producto descontinuado");
                inactivo.setActivo(false);

                when(productClient.obtenerProductos())
                        .thenReturn(List.of(activo, inactivo));

                when(catalogRepository.findAll())
                        .thenReturn(List.of())
                        .thenReturn(List.of(activo, inactivo));

                List<ProductoDTO> resultado = catalogService.verCatalogo();

                assertEquals(1, resultado.size());
                assertEquals("Coca Cola", resultado.get(0).getNombre());
        }

        @Test
        void testVerCatalogoDisponible_esEquivalenteAVerCatalogo() {

                when(productClient.obtenerProductos())
                        .thenReturn(List.of(producto));

                when(catalogRepository.findAll())
                        .thenReturn(List.of())
                        .thenReturn(List.of(producto));

                List<ProductoDTO> resultado = catalogService.verCatalogoDisponible();

                assertEquals(1, resultado.size());
                assertEquals("Coca Cola", resultado.get(0).getNombre());
        }

        @Test
        void testObtenerPorCategoria_filtraPorIdDeCategoria() {

                CategoriaDTO bebidas = new CategoriaDTO(1L, "Bebidas", "Bebidas y jugos");
                CategoriaDTO snacks = new CategoriaDTO(2L, "Snacks", "Papas y galletas");

                ProductoDTO bebida = copiar(producto);
                bebida.setCategoria(bebidas);

                ProductoDTO snack = copiar(producto);
                snack.setId(2L);
                snack.setNombre("Papas fritas");
                snack.setCategoria(snacks);

                when(productClient.obtenerProductos())
                        .thenReturn(List.of(bebida, snack));

                when(catalogRepository.findAll())
                        .thenReturn(List.of(bebida, snack));

                when(categoriaCatalogRepository.findById(1L)).thenReturn(Optional.of(bebidas));
                when(categoriaCatalogRepository.findById(2L)).thenReturn(Optional.of(snacks));

                List<ProductoDTO> resultado = catalogService.obtenerPorCategoria(1L);

                assertEquals(1, resultado.size());
                assertEquals("Coca Cola", resultado.get(0).getNombre());
        }

        @Test
        void testObtenerPorNombre_filtraPorSubcadenaSinImportarMayusculas() {

                ProductoDTO cocaCola = copiar(producto);

                ProductoDTO agua = copiar(producto);
                agua.setId(2L);
                agua.setNombre("Agua mineral");

                when(productClient.obtenerProductos())
                        .thenReturn(List.of(cocaCola, agua));

                when(catalogRepository.findAll())
                        .thenReturn(List.of(cocaCola, agua));

                List<ProductoDTO> resultado = catalogService.obtenerPorNombre("coca");

                assertEquals(1, resultado.size());
                assertEquals("Coca Cola", resultado.get(0).getNombre());
        }

        @Test
        void testObtenerPorNombreCategoria_filtraPorNombreDeCategoria() {

                CategoriaDTO bebidas = new CategoriaDTO(1L, "Bebidas", "Bebidas y jugos");
                CategoriaDTO snacks = new CategoriaDTO(2L, "Snacks", "Papas y galletas");

                ProductoDTO bebida = copiar(producto);
                bebida.setCategoria(bebidas);

                ProductoDTO snack = copiar(producto);
                snack.setId(2L);
                snack.setNombre("Papas fritas");
                snack.setCategoria(snacks);

                when(productClient.obtenerProductos())
                        .thenReturn(List.of(bebida, snack));

                when(catalogRepository.findAll())
                        .thenReturn(List.of(bebida, snack));

                when(categoriaCatalogRepository.findById(1L)).thenReturn(Optional.of(bebidas));
                when(categoriaCatalogRepository.findById(2L)).thenReturn(Optional.of(snacks));

                List<ProductoDTO> resultado = catalogService.obtenerPorNombreCategoria("bebidas");

                assertEquals(1, resultado.size());
                assertEquals("Coca Cola", resultado.get(0).getNombre());
        }

        @Test
        void testObtenerPorNombreCategoria_sinCoincidenciasDevuelveListaVacia() {

                CategoriaDTO bebidas = new CategoriaDTO(1L, "Bebidas", "Bebidas y jugos");
                ProductoDTO bebida = copiar(producto);
                bebida.setCategoria(bebidas);

                when(productClient.obtenerProductos())
                        .thenReturn(List.of(bebida));

                when(catalogRepository.findAll())
                        .thenReturn(List.of(bebida));

                when(categoriaCatalogRepository.findById(anyLong())).thenReturn(Optional.of(bebidas));

                List<ProductoDTO> resultado = catalogService.obtenerPorNombreCategoria("Congelados");

                assertNotNull(resultado);
                assertTrue(resultado.isEmpty());
        }

        @Test
        void testObtenerPorNombreCategoria_propagaErrorDeInventario() {

                when(productClient.obtenerProductos())
                        .thenThrow(new RuntimeException("Error categoría"));

                RuntimeException ex = assertThrows(
                        RuntimeException.class,
                        () -> catalogService.obtenerPorNombreCategoria("Bebidas"));

                assertEquals("Error categoría", ex.getMessage());
        }

        @Test
        void testVerCatalogo_creaCategoriaNuevaSiNoExisteLocalmente() {

                CategoriaDTO bebidas = new CategoriaDTO(1L, "Bebidas", "Bebidas y jugos");
                ProductoDTO conCategoria = copiar(producto);
                conCategoria.setCategoria(bebidas);

                when(productClient.obtenerProductos())
                        .thenReturn(List.of(conCategoria));

                when(catalogRepository.findAll())
                        .thenReturn(List.of())
                        .thenReturn(List.of(conCategoria));

                // La categoría todavía no existe en la BD local de Catalogo
                when(categoriaCatalogRepository.findById(1L)).thenReturn(Optional.empty());
                when(categoriaCatalogRepository.save(bebidas)).thenReturn(bebidas);

                catalogService.verCatalogo();

                verify(categoriaCatalogRepository, times(1)).save(bebidas);
        }

        @Test
        void testVerCatalogo_actualizaCategoriaCuandoNombreODescripcionCambian() {

                CategoriaDTO bebidasDesactualizada = new CategoriaDTO(1L, "Bebida", "desc vieja");
                CategoriaDTO bebidasActual = new CategoriaDTO(1L, "Bebidas", "Bebidas y jugos");

                ProductoDTO conCategoria = copiar(producto);
                conCategoria.setCategoria(bebidasActual);

                when(productClient.obtenerProductos())
                        .thenReturn(List.of(conCategoria));

                when(catalogRepository.findAll())
                        .thenReturn(List.of())
                        .thenReturn(List.of(conCategoria));

                when(categoriaCatalogRepository.findById(1L)).thenReturn(Optional.of(bebidasDesactualizada));
                when(categoriaCatalogRepository.save(bebidasActual)).thenReturn(bebidasActual);

                catalogService.verCatalogo();

                verify(categoriaCatalogRepository, times(1)).save(bebidasActual);
        }

        @Test
        void testVerCatalogo_noActualizaCategoriaCuandoYaEstaAlDia() {

                CategoriaDTO bebidas = new CategoriaDTO(1L, "Bebidas", "Bebidas y jugos");

                ProductoDTO conCategoria = copiar(producto);
                conCategoria.setCategoria(bebidas);

                when(productClient.obtenerProductos())
                        .thenReturn(List.of(conCategoria));

                when(catalogRepository.findAll())
                        .thenReturn(List.of(conCategoria))
                        .thenReturn(List.of(conCategoria));

                when(categoriaCatalogRepository.findById(1L)).thenReturn(Optional.of(bebidas));

                catalogService.verCatalogo();

                verify(categoriaCatalogRepository, never()).save(any());
                verify(catalogRepository, never()).save(any());
        }

        @Test
        void testVerCatalogo_actualizaCuandoSoloCambiaLaCategoriaDelProducto() {

                CategoriaDTO bebidas = new CategoriaDTO(1L, "Bebidas", "Bebidas y jugos");
                CategoriaDTO snacks = new CategoriaDTO(2L, "Snacks", "Papas y galletas");

                ProductoDTO local = copiar(producto);
                local.setCategoria(bebidas);

                ProductoDTO remoto = copiar(producto);
                remoto.setCategoria(snacks); // mismo producto, pero Inventario ahora dice que es Snacks

                when(productClient.obtenerProductos())
                        .thenReturn(List.of(remoto));

                when(catalogRepository.findAll())
                        .thenReturn(List.of(local))
                        .thenReturn(List.of(remoto));

                when(categoriaCatalogRepository.findById(2L)).thenReturn(Optional.of(snacks));

                List<ProductoDTO> resultado = catalogService.verCatalogo();

                assertEquals(2L, resultado.get(0).getCategoria().getId());
                verify(catalogRepository, times(1)).save(remoto);
        }

        @Test
        void testObtenerPorCategoria_sinProductosEnEsaCategoriaDevuelveVacio() {

                CategoriaDTO snacks = new CategoriaDTO(2L, "Snacks", "Papas y galletas");
                ProductoDTO snack = copiar(producto);
                snack.setCategoria(snacks);

                when(productClient.obtenerProductos())
                        .thenReturn(List.of(snack));

                when(catalogRepository.findAll())
                        .thenReturn(List.of(snack));

                when(categoriaCatalogRepository.findById(2L)).thenReturn(Optional.of(snacks));

                List<ProductoDTO> resultado = catalogService.obtenerPorCategoria(99L);

                assertNotNull(resultado);
                assertTrue(resultado.isEmpty());
        }

        @Test
        void testObtenerPorNombre_sinCoincidenciasDevuelveVacio() {

                when(productClient.obtenerProductos())
                        .thenReturn(List.of(producto));

                when(catalogRepository.findAll())
                        .thenReturn(List.of(producto));

                List<ProductoDTO> resultado = catalogService.obtenerPorNombre("inexistente");

                assertNotNull(resultado);
                assertTrue(resultado.isEmpty());
        }
}
