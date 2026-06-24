package com.pruebas.unitarias.controller;

import com.caso3.inventario.Inventario;
import com.caso3.inventario.model.Categoria;
import com.caso3.inventario.model.Producto;
import com.caso3.inventario.repository.ProductoRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
@SpringBootTest(classes = Inventario.class)
@AutoConfigureMockMvc
class InvControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductoRepository repository;

    @BeforeEach
    void limpiar() {
        repository.deleteAll();
    }
    @Test
    void testCrearProducto() throws Exception {

    Producto producto = new Producto();
    producto.setNombre("Mouse Gamer");
    producto.setPrecio(25000.0);
    producto.setStock(10);
    producto.setCategoria(Categoria.ELECTRODOMESTICOS);

    mockMvc.perform(post("/api/inventario/addprod")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(producto)))
            .andExpect(status().isCreated())
            .andExpect(content().string("Producto registrado"));
    }
}