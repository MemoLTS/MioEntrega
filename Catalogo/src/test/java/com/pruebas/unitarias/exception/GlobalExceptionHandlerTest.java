package com.pruebas.unitarias.exception;

import org.junit.jupiter.api.Test;

import com.caso3.catalogo.exception.GlobalExceptionHandler;

import static org.junit.jupiter.api.Assertions.assertNotNull;


class GlobalExceptionHandlerTest {

    @Test
    void testHandlerExists() {
        // Simple test to ensure the handler class can be instantiated
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        assertNotNull(handler);
    }
}

