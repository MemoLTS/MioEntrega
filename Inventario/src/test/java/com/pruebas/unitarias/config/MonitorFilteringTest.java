package com.pruebas.unitarias.config;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.eq;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import com.caso3.inventario.config.MonitoringFilter;
import com.caso3.inventario.dto.LogDTO;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class MonitoringFilterTest {

        @Mock
        private RestTemplate restTemplate;

        @Mock
        private FilterChain filterChain;

        @Mock
        private HttpServletRequest request;

        @Mock
        private HttpServletResponse response;

        @InjectMocks
        private MonitoringFilter filter;

        @Test
        void testGeneraLogParaPost() throws Exception {
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/api/inventario/addprod");
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        when(response.getStatus()).thenReturn(201);
        filter.doFilter(request, response, filterChain);
        verify(filterChain)
                .doFilter(request, response);
        verify(restTemplate)
                .postForObject(
                        eq("http://localhost:8091/logs"),
                        any(LogDTO.class),
                        eq(String.class)
                );
        }
        @Test
        void testNoGeneraLogCuandoMetodoEsGet() throws Exception {
        when(request.getMethod()).thenReturn("GET");
        filter.doFilter(request, response, filterChain);
        verify(filterChain)
                .doFilter(request, response);
        verify(restTemplate, never())
                .postForObject(
                        anyString(),
                        any(),
                        eq(String.class)
                );
        }
        @Test
        void testCatchCuandoFallaEnvioLog() throws Exception {
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/api/inventario/addprod");
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        when(response.getStatus()).thenReturn(201);
        doThrow(new RuntimeException("Error"))
                .when(restTemplate)
                .postForObject(
                        anyString(),
                        any(LogDTO.class),
                        eq(String.class)
                );
        assertDoesNotThrow(() ->
                filter.doFilter(request, response, filterChain)
        );
        verify(filterChain)
                .doFilter(request, response);
        verify(restTemplate)
                .postForObject(
                        eq("http://localhost:8091/logs"),
                        any(LogDTO.class),
                        eq(String.class)
                );
        }
}

