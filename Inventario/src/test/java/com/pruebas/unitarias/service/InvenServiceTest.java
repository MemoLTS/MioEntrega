package com.pruebas.unitarias.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.caso3.inventario.model.Categoria;
import com.caso3.inventario.model.Producto;
import com.caso3.inventario.service.InvService;
import com.caso3.inventario.repository.ProductoRepository;

@ExtendWith(MockitoExtension.class)
class InvenServiceTest {

        @Mock
        private ProductoRepository repository;

        @InjectMocks
        private InvService service;

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
        }
        @ExtendWith(MockitoExtension.class)


        @Test
        void testReadById_ok() {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Laptop");

        when(repository.findById(1L))
                .thenReturn(Optional.of(producto));

        Optional<Producto> result = service.readByid(1L);

        assertTrue(result.isPresent());
        assertEquals("Laptop", result.get().getNombre());

        verify(repository).findById(1L);
        }
        @Test
        void testReadById_notFound() {

        when(repository.findById(99L))
                .thenReturn(Optional.empty());

        Optional<Producto> result = service.readByid(99L);

        assertTrue(result.isEmpty());

        verify(repository).findById(99L);
        }
        @Test
        void testRegisterProductoNull() {
                IllegalArgumentException ex = assertThrows(
                        IllegalArgumentException.class,
                        () -> service.register(null)
                );

                assertEquals("Producto no puede ser nulo", ex.getMessage());
                }

        @Test
        void testRegisterNombreNull() {
                Producto producto = new Producto();
                producto.setNombre(null);

                IllegalArgumentException ex = assertThrows(
                        IllegalArgumentException.class,
                        () -> service.register(producto)
                );

                assertEquals("El nombre del producto es obligatorio", ex.getMessage());
        }

        @Test
        void testRegisterNombreVacio() {
                Producto producto = new Producto();
                producto.setNombre("");

                IllegalArgumentException ex = assertThrows(
                        IllegalArgumentException.class,
                        () -> service.register(producto)
                );

                assertEquals("El nombre del producto es obligatorio", ex.getMessage());
        }

        @Test
        void testRegisterNombreMenorATresCaracteres() {
                Producto producto = new Producto();
                producto.setNombre("AB");

                IllegalArgumentException ex = assertThrows(
                        IllegalArgumentException.class,
                        () -> service.register(producto)
                );

                assertEquals("El nombre debe tener al menos 3 caracteres", ex.getMessage());
        }

        @Test
        void testRegisterPrecioNull() {
                Producto producto = new Producto();
                producto.setNombre("pepsi");
                producto.setPrecio(null);

                IllegalArgumentException ex = assertThrows(
                        IllegalArgumentException.class,
                        () -> service.register(producto)
                );

                assertEquals("El precio debe ser mayor o igual a 0", ex.getMessage());
        }

        @Test
        void testRegisterPrecioNegativo() {
                Producto producto = new Producto();
                producto.setNombre("Laptop");
                producto.setPrecio(-1000.0);
                IllegalArgumentException ex = assertThrows(
                        IllegalArgumentException.class,
                        () -> service.register(producto)
                );

                assertEquals("El precio debe ser mayor o igual a 0", ex.getMessage());
        }

        @Test
        void testRegisterCategoriaNull() {
                Producto producto = new Producto();
                producto.setNombre("Pepsi");
                producto.setPrecio(1500.0);
                producto.setStock(10);
                producto.setCategoria(null);

                IllegalArgumentException ex = assertThrows(
                        IllegalArgumentException.class,
                        () -> service.register(producto)
                );

                assertEquals("La categoría es obligatoria", ex.getMessage());
        }

        @Test
        void testRegisterStockNegativo() {
                Producto producto = new Producto();
                producto.setNombre("Laptop");
                producto.setPrecio(1000.0);
                producto.setStock(-1);
                producto.setCategoria(Categoria.ELECTRODOMESTICOS);

                IllegalArgumentException ex = assertThrows(
                        IllegalArgumentException.class,
                        () -> service.register(producto)
                );

                assertEquals("El stock no puede ser negativo", ex.getMessage());
                }
        @Test
        void testUpdateStockCantidadNegativa() {
                IllegalArgumentException ex = assertThrows(
                        IllegalArgumentException.class,
                        () -> service.updateStock(1L, -5)
                );

                assertEquals("La cantidad no puede ser negativa", ex.getMessage());
        }

        @Test
        void testGuardarProducto() {
                Producto producto = new Producto(null,"laptop", (double) 2000000, 20, Categoria.ELECTRODOMESTICOS);
                Producto productoGuardado = new Producto(1L, "laptop", (double)2000000, 20, Categoria.ELECTRODOMESTICOS);

                when(repository.save(producto)).thenReturn(productoGuardado);

                Producto resultado = service.register(producto);
                assertThat(resultado).isEqualTo(productoGuardado);
                assertThat(resultado.getId()).isEqualTo(1L);
                assertThat(resultado.getNombre()).isEqualTo("laptop");

                verify(repository).save(producto);
        }
        
        @Test
        void testUpdateStockExitoso() {

                Producto producto = new Producto();
                producto.setId(1L);
                producto.setNombre("Laptop");
                producto.setPrecio(1000.0);
                producto.setStock(5);
                producto.setCategoria(Categoria.ELECTRODOMESTICOS);

                when(repository.findById(1L))
                        .thenReturn(Optional.of(producto));

                when(repository.save(any(Producto.class)))
                        .thenAnswer(invocation -> invocation.getArgument(0));

                Producto resultado = service.updateStock(1L, 20);

                assertEquals(20, resultado.getStock());

                verify(repository).findById(1L);
                verify(repository).save(producto);
        }
        @Test
        void testListarProductos() {
                Producto producto = new Producto(1L,"laptop", (double) 2000000, 20, Categoria.ELECTRODOMESTICOS);
                List<Producto> productos = new ArrayList<>();
                productos.add(producto);

                when(repository.findAll()).thenReturn(productos);

                List<Producto> resultado = service.readAllProd();

                assertThat(resultado).hasSize(1);
                assertThat(resultado).contains(producto);

                verify(repository).findAll();
        }
        @Test
        void testRegisterExitoso() {

                Producto producto = new Producto();
                producto.setNombre("Laptop");
                producto.setPrecio(1000.0);
                producto.setStock(10);
                producto.setCategoria(Categoria.ELECTRODOMESTICOS);

                when(repository.save(any(Producto.class)))
                        .thenReturn(producto);

                Producto resultado = service.register(producto);

                assertNotNull(resultado);
                assertEquals(producto, resultado);

                verify(repository).save(producto);
        }

        @Test
        void testListarProductosPorCategoria() {
                Producto producto = new Producto(
                        1L,
                        "laptop",
                        2000000.0,
                        20,
                        Categoria.ELECTRODOMESTICOS
                );

                List<Producto> productos = new ArrayList<>();
                productos.add(producto);

                when(repository.findByCategoria(Categoria.ELECTRODOMESTICOS))
                        .thenReturn(productos);

                List<Producto> resultado =
                        service.readByCategoria(Categoria.ELECTRODOMESTICOS);

                assertThat(resultado).hasSize(1);
                assertThat(resultado).contains(producto);

                verify(repository).findByCategoria(Categoria.ELECTRODOMESTICOS);
                }

        @Test
        void testUpdateStock() {

                Producto producto = new Producto(
                        1L,
                        "laptop",
                        2000000.0,
                        20,
                        Categoria.ELECTRODOMESTICOS
                );

                when(repository.findById(1L))
                        .thenReturn(Optional.of(producto));

                when(repository.save(any(Producto.class)))
                        .thenAnswer(inv -> inv.getArgument(0));

                Producto resultado = service.updateStock(1L, 2000);

                assertNotNull(resultado);
                assertEquals(2000, resultado.getStock());

                verify(repository).findById(1L);
                verify(repository).save(producto);
        }
        @Test
        void testProdUpdateStock() {

                Producto producto = new Producto(
                        1L,
                        "laptop",
                        2000000.0,
                        20,
                        Categoria.ELECTRODOMESTICOS
                );

                when(repository.findById(1L))
                        .thenReturn(Optional.of(producto));

                when(repository.save(any(Producto.class)))
                        .thenAnswer(invocation -> invocation.getArgument(0));

                Producto resultado = service.updateStock(1L, 2000);

                assertEquals(2000, resultado.getStock());

                verify(repository).findById(1L);
                verify(repository).save(producto);
        }

        @Test
        void testActualizarProducto() {
                Producto productoExistente = new Producto(1L,"laptop", (double) 2000000, 20, Categoria.ELECTRODOMESTICOS);
                Producto productoActualizado = new Producto(1L,"laptop", (double) 2000000, 20, Categoria.ELECTRODOMESTICOS);

                when(repository.findById(1L)).thenReturn(Optional.of(productoExistente));
                when(repository.save(any(Producto.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

                Producto resultado = service.updateProducto(1L, productoActualizado);

                assertThat(resultado.getNombre()).isEqualTo("laptop");
                assertThat(resultado.getCategoria()).isEqualTo(Categoria.ELECTRODOMESTICOS);
                assertThat(resultado.getPrecio()).isEqualTo(2000000.0);

                verify(repository).findById(1L);
                verify(repository).save(productoExistente);
        }
        @Test
        void testUpdateStockIdNull() {

                IllegalArgumentException ex = assertThrows(
                        IllegalArgumentException.class,
                        () -> service.updateStock(null, 10)
                );

                assertEquals("El id no puede ser nulo", ex.getMessage());
        }

        @Test
        void testEliminarProducto() {

                when(repository.existsById(1L))
                        .thenReturn(true);

                service.deleteById(1L);

                verify(repository).existsById(1L);
                verify(repository).deleteById(1L);
        }

        @Test
        void testDeleteByIdIdNull() {

                IllegalArgumentException ex = assertThrows(
                        IllegalArgumentException.class,
                        () -> service.deleteById(null)
                );

                assertEquals("El id no puede ser nulo", ex.getMessage());
        }
        @Test
        void testDeleteByIdProductoNoExiste() {

                when(repository.existsById(1L))
                        .thenReturn(false);

                ResponseStatusException ex = assertThrows(
                        ResponseStatusException.class,
                        () -> service.deleteById(1L)
                );

                assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());

                verify(repository).existsById(1L);
        }
        @Test
        void testDeleteByIdExitoso() {

                when(repository.existsById(1L))
                        .thenReturn(true);

                service.deleteById(1L);

                verify(repository).existsById(1L);
                verify(repository).deleteById(1L);
        }

        @Test
        void testObtenerPorCategoria() {

                Producto p1 = new Producto(
                        1L,
                        "Laptop",
                        2000000.0,
                        20,
                        Categoria.ELECTRODOMESTICOS
                );

                Producto p2 = new Producto(
                        2L,
                        "Mesa",
                        50000.0,
                        10,
                        Categoria.HOGAR
                );

                when(repository.findAll())
                        .thenReturn(List.of(p1, p2));

                List<Producto> resultado =
                        service.obtenerPorCategoria("HOGAR");

                assertEquals(1, resultado.size());
                assertEquals("Mesa", resultado.get(0).getNombre());

                verify(repository).findAll();
        }

        @Test
        void testObtenerPorCategoriaSinResultados() {

                Producto p1 = new Producto(
                        1L,
                        "Mesa",
                        50000.0,
                        10,
                        Categoria.HOGAR
                );

                when(repository.findAll())
                        .thenReturn(List.of(p1));

                List<Producto> resultado =
                        service.obtenerPorCategoria("electrodomesticos");

                assertTrue(resultado.isEmpty());

                verify(repository).findAll();
        }
        @Test
        void testObtenerPorCategoriaCategoriaNull() {

                Producto producto = new Producto();
                producto.setId(1L);
                producto.setNombre("Laptop");
                producto.setPrecio(2000000.0);
                producto.setStock(20);
                producto.setCategoria(null);

                when(repository.findAll())
                        .thenReturn(List.of(producto));

                List<Producto> resultado =
                        service.obtenerPorCategoria("electrodomesticos");

                assertTrue(resultado.isEmpty());

                verify(repository).findAll();
        }
        @Test
        void testBuscarPorCategoria() {

                Producto producto = new Producto(
                        1L,
                        "Laptop",
                        2000000.0,
                        20,
                        Categoria.ELECTRODOMESTICOS
                );

                when(repository.findByCategoria(Categoria.ELECTRODOMESTICOS))
                        .thenReturn(List.of(producto));

                List<Producto> resultado =
                        service.buscarPorCategoria(Categoria.ELECTRODOMESTICOS);

                assertEquals(1, resultado.size());

                verify(repository)
                        .findByCategoria(Categoria.ELECTRODOMESTICOS);
        }
}