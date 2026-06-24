package com.pruebas.unitarias.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.caso3.catalogo.controller.CatalogController;

import com.caso3.catalogo.service.CatalogService;
@SpringBootTest(classes = CatalogController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CatalogControllerIT {

        @SuppressWarnings("unused")
        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private CatalogService service;
}