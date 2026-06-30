package com.pruebas.unitarias.controller;

import com.caso3.inventario.Inventario;
import com.caso3.inventario.controller.InvController;
import com.caso3.inventario.dto.LogDTO;
import com.caso3.inventario.dto.StockResponse;
import com.caso3.inventario.model.Categoria;
import com.caso3.inventario.model.Producto;
import com.caso3.inventario.service.InvService;
import com.caso3.inventario.service.LogService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(InvController.class)
@ContextConfiguration(classes = Inventario.class)
class InvControllerTest {

        @Autowired
        private MockMvc mockMvc;
        @Autowired
        private ObjectMapper objectMapper;
        @SuppressWarnings("removal")
        @MockBean
        private InvService service;

        @SuppressWarnings("removal")
        @MockBean
        private LogService logservice;
        private Producto crearProducto() {
        return new Producto(
                1L,
                "Laptop",
                2000000.0,
                20,
                Categoria.ELECTRODOMESTICOS);
        }

        @Test
        void testListarLogs() throws Exception {
        List<LogDTO> logs = new ArrayList<>();
        when(logservice.listar()).thenReturn(logs);
        mockMvc.perform(get("/api/inventario/logs"))
                .andExpect(status().isOk());
        verify(logservice).listar();
        }
        @Test
        void testConsultarStock() throws Exception {
                StockResponse response = new StockResponse(
                        1L,
                        "Mouse",
                        20
                );
                when(service.consultarStock(1L))
                        .thenReturn(response);
                mockMvc.perform(get("/api/inventario/stock/1"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.idProducto").value(1))
                        .andExpect(jsonPath("$.nombre").value("Mouse"))
                        .andExpect(jsonPath("$.stock").value(20));
                verify(service).consultarStock(1L);
        }
        @Test
        void testGetProductos() throws Exception {
                Producto producto = crearProducto();
                when(service.readAllProd())
                        .thenReturn(List.of(producto));
                mockMvc.perform(get("/api/inventario/productos"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$[0].id").value(1))
                        .andExpect(jsonPath("$[0].nombre").value("Laptop"));
        }

        @Test
        void testPostProductoCreado() throws Exception {
                Producto producto = crearProducto();
                when(service.register(any(Producto.class)))
                        .thenReturn(producto);
                mockMvc.perform(post("/api/inventario/addprod")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                        .andExpect(status().isCreated())
                        .andExpect(content().string("Producto registrado"));
        }

        @Test
        void testPostProductoError() throws Exception {
                Producto producto = crearProducto();
                when(service.register(any(Producto.class)))
                        .thenReturn(null);
                mockMvc.perform(post("/api/inventario/addprod")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().string("Error al registrar producto"));
        }

        @Test
        void testGetProductoPorId() throws Exception {
                Producto producto = crearProducto();
                when(service.readByid(1L))
                        .thenReturn(Optional.of(producto));
                mockMvc.perform(get("/api/inventario/productos/1"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.nombre").value("Laptop"));
        }

        @Test
        void testUpdateProductoOk() throws Exception {
                Producto existente = crearProducto();
                Producto actualizado = new Producto(
                        1L,
                        "Laptop Gamer",
                        2500000.0,
                        20,
                        Categoria.ELECTRODOMESTICOS);
                when(service.readAllProd())
                        .thenReturn(List.of(existente));
                when(service.register(any(Producto.class)))
                        .thenReturn(actualizado);
                mockMvc.perform(put("/api/inventario/updateprod/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                        .andExpect(status().isOk())
                        .andExpect(content().string("Producto actualizado"));
        }

        @Test
        void testUpdateProductoNotFound() throws Exception {
                Producto producto = crearProducto();
                when(service.readAllProd())
                        .thenReturn(List.of());
                mockMvc.perform(put("/api/inventario/updateprod/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                        .andExpect(status().isNotFound())
                        .andExpect(content().string("Producto no encontrado"));
        }

        @Test
        void testUpdateStockOk() throws Exception {
                Producto producto = new Producto();
                producto.setId(1L);
                producto.setStock(50);
                when(service.updateStock(1L, 50))
                        .thenReturn(producto);
                mockMvc.perform(put("/api/inventario/updateprod/1/50"))
                        .andExpect(status().isOk());
        }

        @Test
        void testUpdateStockNotFound() throws Exception {
                doThrow(new RuntimeException())
                        .when(service)
                        .updateStock(1L, 50);
                mockMvc.perform(put("/api/inventario/updateprod/1/50"))
                        .andExpect(status().isNotFound())
                        .andExpect(content().string("Producto no encontrado"));
        }

        @Test
        void testDeleteProductoOk() throws Exception {
                doNothing().when(service).deleteById(1L);
                mockMvc.perform(delete("/api/inventario/deleteprod/1"))
                        .andExpect(status().isOk())
                        .andExpect(content().string("Producto eliminado"));
        }

        @Test
        void testDeleteProductoNotFound() throws Exception {
                doThrow(new RuntimeException())
                        .when(service)
                        .deleteById(1L);
                mockMvc.perform(delete("/api/inventario/deleteprod/1"))
                        .andExpect(status().isNotFound())
                        .andExpect(content().string("Producto no encontrado"));
        }

        @Test
        void testGetProductosPorCategoria() throws Exception {
                Producto producto = crearProducto();
                when(service.buscarPorCategoria(Categoria.ELECTRODOMESTICOS))
                        .thenReturn(List.of(producto));
                mockMvc.perform(
                        get("/api/inventario/productos/buscar/categoria/ELECTRODOMESTICOS"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$[0].nombre").value("Laptop"));
        }

        @Test
        void testGetProductoOrdenadoPorCategoria() throws Exception {
                Producto producto = crearProducto();
                when(service.buscarPorCategoria(Categoria.ELECTRODOMESTICOS))
                        .thenReturn(List.of(producto));
                mockMvc.perform(
                        get("/api/inventario/productos/ordenados/categoria/ELECTRODOMESTICOS"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$[0].nombre").value("Laptop"));
        }

        @Test
        void testGetPorNombre() throws Exception {
                Producto producto = crearProducto();
                when(service.readAllProd())
                        .thenReturn(List.of(producto));
                mockMvc.perform(
                        get("/api/inventario/productos/nombre/Lap"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$[0].nombre").value("Laptop"));
        }
        @Test
        void testVerificarDisponibilidad() throws Exception {
                StockResponse response =
                        new StockResponse(1L, "Leche", 20, true);
                when(service.verificarDisponibilidad(1L, 5))
                        .thenReturn(response);
                mockMvc.perform(
                        get("/api/inventario/disponibilidad/1/5"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.idProducto").value(1))
                        .andExpect(jsonPath("$.nombre").value("Leche"))
                        .andExpect(jsonPath("$.stock").value(20))
                        .andExpect(jsonPath("$.disponible").value(true));
                verify(service).verificarDisponibilidad(1L, 5);
        }

        @Test
        void testIngresarStock() throws Exception {
                doNothing().when(service)
                        .ingresarStock(1L, 2L, 10);
                mockMvc.perform(post("/api/inventario/ingresar-stock/1/2/10"))
                        .andExpect(status().isOk())
                        .andExpect(content().string("Stock actualizado y log registrado"));
        }

        @Test
        void testVerificarBajoStock() throws Exception {
                StockResponse response = new StockResponse();
                response.setIdProducto(1L);
                response.setNombre("Leche");
                response.setStock(2);
                response.setAlertaBajoStock(true);
                when(service.verificarBajoStock(1L))
                        .thenReturn(response);
                mockMvc.perform(get("/api/inventario/alerta/1"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.alertaBajoStock").value(true));
                verify(service).verificarBajoStock(1L);
        }

        @Test
        void testVerificarDisponibilidadNotFound() throws Exception {
        when(service.verificarDisponibilidad(1L, 5))
                .thenThrow(new RuntimeException("Producto no encontrado"));
        mockMvc.perform(get("/api/inventario/disponibilidad/1/5"))
                .andExpect(status().isNotFound());
        verify(service).verificarDisponibilidad(1L, 5);
        }
}