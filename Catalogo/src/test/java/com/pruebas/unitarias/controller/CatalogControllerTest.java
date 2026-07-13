package com.pruebas.unitarias.controller;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.caso3.catalogo.Catalogo;
import com.caso3.catalogo.dto.ProductoDTO;
import com.caso3.catalogo.service.CatalogService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = Catalogo.class)
@AutoConfigureMockMvc
class CatalogControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private CatalogService service;

        @Test
        void testVerCatalogo() throws Exception {
                when(service.verCatalogo()).thenReturn(List.of(new ProductoDTO()));

                mockMvc.perform(get("/api/v1/catalogo/ver"))
                        .andExpect(status().isOk());
        }

        @Test
        void testVerCatalogoDisponibles() throws Exception {
                when(service.verCatalogoDisponible()).thenReturn(List.of(new ProductoDTO()));

                mockMvc.perform(get("/api/v1/catalogo/ver/disponibles"))
                        .andExpect(status().isOk());
        }

        @Test
        void testPorCategoria() throws Exception {
                when(service.obtenerPorCategoria(1L)).thenReturn(List.of(new ProductoDTO()));

                mockMvc.perform(get("/api/v1/catalogo/PorCategoria/1"))
                        .andExpect(status().isOk());
        }

        @Test
        void testPorNombre() throws Exception {
                when(service.obtenerPorNombre("laptop")).thenReturn(List.of(new ProductoDTO()));

                mockMvc.perform(get("/api/v1/catalogo/PorNombre/laptop"))
                        .andExpect(status().isOk());
        }

        @Test
        void testPorNombreCategoria() throws Exception {
                when(service.obtenerPorNombreCategoria("Bebidas")).thenReturn(List.of(new ProductoDTO()));

                mockMvc.perform(get("/api/v1/catalogo/PorNombreCategoria/Bebidas"))
                        .andExpect(status().isOk());
        }
}
