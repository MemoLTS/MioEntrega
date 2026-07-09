package com.caso3.backup.service;

import com.caso3.backup.model.Backup;
import com.caso3.backup.repository.BackupRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SuppressWarnings("unused")
@ExtendWith(MockitoExtension.class)
class BackupServiceTest {

        @Mock
        private BackupRepository repository;

        @InjectMocks
        private BackupService service;

        @Test
        void crearBackupCorrectamente(){
        Backup backup = new Backup();
        backup.setTipo("MANUAL");
        when(repository.save(any()))
                .thenReturn(backup);
        Backup resultado =
                service.crearBackup("MANUAL");
        assertEquals(
                "MANUAL",
                resultado.getTipo()
        );
        }


        @Test
        void obtenerUltimoBackup(){
                Backup backup = new Backup();
                backup.setTipo("INICIAL");
                when(
                repository.findTopByOrderByFechaCreacionDesc()
                )
                .thenReturn(backup);
                Backup resultado =
                        service.obtenerUltimo();
                assertNotNull(resultado);
                assertEquals(
                        "INICIAL",
                        resultado.getTipo()
                );
        }

        @Test
        void testExisteBackupCuandoHayRegistros(){
                when(repository.count()).thenReturn(5L);
                boolean resultado = service.existeBackup();
                assertTrue(resultado);
                verify(repository).count();
        }



        @Test
        void testExisteBackupCuandoNoHayRegistros(){
                when(repository.count()).thenReturn(0L);
                boolean resultado = service.existeBackup();
                assertFalse(resultado);

                verify(repository).count();

        }



        @Test
        void testListarBackups(){
                Backup backup = new Backup();
                backup.setTipo("MANUAL");

                when(repository.findAll())
                        .thenReturn(List.of(backup));
                List<Backup> resultado = service.listar();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("MANUAL",
                resultado.get(0).getTipo());
        verify(repository).findAll();
        }



        @Test
        void testValidarBackupInicialCuandoNoExisteBackup(){

        when(repository.count())
                .thenReturn(0L);
        service.validarBackupInicial();

        verify(repository).count();

        }



        @Test
        void testValidarBackupInicialCuandoYaExisteBackup(){
                when(repository.count())
                        .thenReturn(2L);
                service.validarBackupInicial();
                verify(repository).count();

        }
}