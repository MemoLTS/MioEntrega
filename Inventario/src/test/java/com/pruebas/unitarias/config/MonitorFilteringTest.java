package com.pruebas.unitarias.config;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.caso3.inventario.config.MonitoringFilter;
import com.caso3.inventario.dto.LogDTO;
import com.caso3.inventario.service.LogService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
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

