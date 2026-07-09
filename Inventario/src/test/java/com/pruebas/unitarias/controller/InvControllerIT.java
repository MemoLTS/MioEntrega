package com.pruebas.unitarias.controller;

import com.caso3.inventario.Inventario;
import com.caso3.inventario.model.Categoria;
import com.caso3.inventario.model.Producto;
import com.caso3.inventario.repository.CategoriaRepository;
import com.caso3.inventario.repository.ProductoRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = Inventario.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class InvControllerIT {


    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private ProductoRepository productoRepository;


    @Autowired
    private CategoriaRepository categoriaRepository;


    @BeforeEach
    void limpiarBD() {

        productoRepository.deleteAllInBatch();
        categoriaRepository.deleteAllInBatch();

    }


    @Test
    void testCrearProducto() throws Exception {


        Categoria categoria = new Categoria();

        categoria.setNombre("Periféricos");
        categoria.setDescripcion("Accesorios de computador");


        categoria = categoriaRepository.save(categoria);


        Producto producto = new Producto();

        producto.setNombre("Mouse Gamer");
        producto.setPrecio(25000.0);
        producto.setStock(10);
        producto.setCategoria(categoria);



        mockMvc.perform(post("/api/inventario/addprod")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))

                .andExpect(status().isCreated())
                .andExpect(content().string("Producto registrado"));

    }
}