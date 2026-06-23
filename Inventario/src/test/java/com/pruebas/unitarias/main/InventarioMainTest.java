package com.pruebas.unitarias.main;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.caso3.inventario.Inventario;

@SpringBootTest(classes = Inventario.class)
@ActiveProfiles("test")
class InvenatarioMainTests {

        @Test
        void contextLoads() {
        }

        }
