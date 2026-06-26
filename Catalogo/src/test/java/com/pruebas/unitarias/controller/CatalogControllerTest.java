package com.pruebas.unitarias.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.caso3.catalogo.Catalogo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest(classes = Catalogo.class)
@AutoConfigureMockMvc
class CatalogControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Test
        void testVerCatalogo() throws Exception {
                mockMvc.perform(get("/catalogo/ver"))
                        .andExpect(status().isOk());
        }

        @Test
        void testPorCategoria() throws Exception {
                mockMvc.perform(get("/catalogo/PorCategoria/electronica"))
                        .andExpect(status().isOk());
        }

        @Test
        void testPorNombre() throws Exception {
                mockMvc.perform(get("/catalogo/PorNombre/laptop"))
                        .andExpect(status().isOk());
        }
}