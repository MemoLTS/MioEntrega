package com.caso3.backup.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.caso3.backup.model.BackupConfig;
import com.caso3.backup.repository.BackupConfigRepository;
@ExtendWith(MockitoExtension.class)
class BackupConfigServiceTest {

        @Mock
        private BackupConfigRepository repository;

        @InjectMocks
        private BackupConfigService service;


        @Test
        void obtenerCuandoExisteConfig() {

                BackupConfig config = new BackupConfig();
                config.setId(1L);
                config.setActivo(false);

                when(repository.findById(1L))
                        .thenReturn(Optional.of(config));


                BackupConfig resultado = service.obtener();


                assertNotNull(resultado);
                assertEquals(1L, resultado.getId());
                assertFalse(resultado.isActivo());

                verify(repository)
                        .findById(1L);
        }


        @Test
        void obtenerCuandoNoExisteConfigDebeCrear() {

                when(repository.findById(1L))
                        .thenReturn(Optional.empty());


                BackupConfig guardada = new BackupConfig();
                guardada.setId(1L);
                guardada.setActivo(false);

                when(repository.save(any(BackupConfig.class)))
                        .thenReturn(guardada);


                BackupConfig resultado = service.obtener();


                assertNotNull(resultado);
                assertEquals(1L, resultado.getId());
                assertFalse(resultado.isActivo());


                verify(repository)
                        .findById(1L);

                verify(repository)
                        .save(any(BackupConfig.class));
        }


        @Test
        void activarDebeCambiarEstado() {

                BackupConfig config = new BackupConfig();
                config.setId(1L);
                config.setActivo(false);


                when(repository.findById(1L))
                        .thenReturn(Optional.of(config));

                when(repository.save(any(BackupConfig.class)))
                        .thenReturn(config);


                BackupConfig resultado = service.activar();


                assertTrue(resultado.isActivo());


                verify(repository)
                        .save(config);
        }


        @Test
        void desactivarDebeCambiarEstado() {

                BackupConfig config = new BackupConfig();
                config.setId(1L);
                config.setActivo(true);


                when(repository.findById(1L))
                        .thenReturn(Optional.of(config));

                when(repository.save(any(BackupConfig.class)))
                        .thenReturn(config);


                BackupConfig resultado = service.desactivar();


                assertFalse(resultado.isActivo());


                verify(repository)
                        .save(config);
        }
}