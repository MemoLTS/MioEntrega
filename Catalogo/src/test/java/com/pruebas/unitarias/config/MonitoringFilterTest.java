package com.pruebas.unitarias.config;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;


import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.caso3.catalogo.Catalogo;
import com.caso3.catalogo.config.MonitoringFilter;
import com.caso3.catalogo.dto.LogDTO;
import com.caso3.catalogo.service.LogService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SpringBootTest(classes = Catalogo.class)
@AutoConfigureMockMvc
class MonitoringFilterTest {

        @Mock
        private LogService logService;

        @Mock
        private FilterChain filterChain;

        @Mock
        private HttpServletRequest request;

        @Mock
        private HttpServletResponse response;

        @InjectMocks
        private MonitoringFilter filter;
        @Test
        void testFilterPost() throws Exception {
                when(request.getMethod()).thenReturn("POST");
                when(request.getRequestURI()).thenReturn("/catalogo/test");
                when(response.getStatus()).thenReturn(200);
                when(request.getRemoteAddr()).thenReturn("127.0.0.1");

                filter.doFilter(request, response, filterChain);

                verify(filterChain).doFilter(request, response);
                verify(logService).guardar(any());
        }

        @Test
        void testDoFilterPost() throws Exception {
                when(request.getMethod()).thenReturn("POST");
                when(request.getRequestURI()).thenReturn("/api/inventario/addprod");
                when(request.getRemoteAddr()).thenReturn("127.0.0.1");
                when(response.getStatus()).thenReturn(200);
                filter.doFilter(request, response, filterChain);
                verify(filterChain).doFilter(request, response);
                verify(logService).guardar(any(LogDTO.class));
        }
        @Test
        void testGeneraLogParaPost() throws Exception {
                when(request.getMethod()).thenReturn("POST");
                when(request.getRequestURI()).thenReturn("/api/inventario/addprod");
                when(request.getRemoteAddr()).thenReturn("127.0.0.1");
                when(response.getStatus()).thenReturn(201);
                filter.doFilter(request, response, filterChain);
                verify(filterChain)
                        .doFilter(request, response);
                verify(logService).guardar(any(LogDTO.class));
        }

        @Test
                void testNoGeneraLogCuandoMetodoEsGet() throws Exception {
                when(request.getMethod()).thenReturn("GET");
                filter.doFilter(request, response, filterChain);
                verify(filterChain).doFilter(request, response);
                verify(logService, never()).guardar(any(LogDTO.class));
        }
        @Test
        void testCatchCuandoFallaEnvioLog() throws Exception {
                when(request.getMethod()).thenReturn("POST");
                when(request.getRequestURI()).thenReturn("/api/inventario/addprod");
                when(request.getRemoteAddr()).thenReturn("127.0.0.1");
                when(response.getStatus()).thenReturn(200);
                when(logService.guardar(any(LogDTO.class)))
                        .thenThrow(new RuntimeException("Error"));
                assertDoesNotThrow(() ->
                        filter.doFilter(request, response, filterChain));
                verify(filterChain).doFilter(request, response);
                verify(logService).guardar(any(LogDTO.class));
                }
}

