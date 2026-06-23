package com.pruebas.unitarias.main;


import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

import com.caso3.catalogo.Catalogo;

@SpringBootTest(classes = Catalogo.class)
class ApiApplicationTests {

    @Test
    void testApplicationClass() {
        assertNotNull(new Catalogo());
    }
}