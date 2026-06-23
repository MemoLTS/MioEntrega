package com.pruebas.unitarias.controller;

import com.caso3.inventario.Inventario;

import com.caso3.inventario.service.InvService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest(classes = Inventario.class)
@AutoConfiguration
@ActiveProfiles("test")
class InvControllerIT {

    @SuppressWarnings("unused")
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvService service;
}
