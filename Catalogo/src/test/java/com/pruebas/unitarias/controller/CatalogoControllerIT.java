package com.pruebas.unitarias.controller;

import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import com.caso3.catalogo.Catalogo;
import com.caso3.catalogo.dto.ProductoDTO;
import com.caso3.catalogo.service.CatalogService;


@SpringBootTest(classes = Catalogo.class)
@AutoConfigureMockMvc
class CatalogoControllerIT {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private CatalogService service;

        @Test
        void testVerCatalogo() throws Exception {

                List<ProductoDTO> lista = List.of(new ProductoDTO());

                when(service.verCatalogo()).thenReturn(lista);

                mockMvc.perform(get("/api/v1/catalogo/ver")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.length()").value(1));
        }

        @Test
        void testVerCatalogoDisponibles() throws Exception {

                when(service.verCatalogoDisponible())
                        .thenReturn(List.of(new ProductoDTO()));

                mockMvc.perform(get("/api/v1/catalogo/ver/disponibles")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
        }

        @Test
        void testPorCategoria() throws Exception {

                when(service.obtenerPorCategoria(1L))
                        .thenReturn(List.of(new ProductoDTO()));

                mockMvc.perform(get("/api/v1/catalogo/PorCategoria/1")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
        }

        @Test
        void testPorNombre() throws Exception {

                when(service.obtenerPorNombre("Mouse"))
                        .thenReturn(List.of(new ProductoDTO()));

                mockMvc.perform(get("/api/v1/catalogo/PorNombre/Mouse")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
        }

        @Test
        void testPorNombreCategoria() throws Exception {

                when(service.obtenerPorNombreCategoria("Bebidas"))
                        .thenReturn(List.of(new ProductoDTO()));

                mockMvc.perform(get("/api/v1/catalogo/PorNombreCategoria/Bebidas")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
        }

        @Test
        void testVerCatalogo_listaVacia() throws Exception {

                when(service.verCatalogo()).thenReturn(List.of());

                mockMvc.perform(get("/api/v1/catalogo/ver")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.length()").value(0));
        }
}
