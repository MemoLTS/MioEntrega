package com.pruebas.unitarias.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.caso3.inventario.service.BackupService;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.caso3.inventario.model.BackupLogs;
import com.caso3.inventario.repository.BackupLogRepository;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class BackupServiceTest {
        @Spy
        @InjectMocks
        private BackupService backupService;
        @Mock
        private BackupLogRepository backupLogRepository;
        @Mock
        private Process process;
        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
        }
        @ExtendWith(MockitoExtension.class)

        @Test
        void testStartProcess() throws Exception {
                ProcessBuilder pb = mock(ProcessBuilder.class);
                when(pb.start()).thenReturn(process);
                Process result = backupService.startProcess(pb);
                assertNotNull(result);
                assertEquals(process, result);
                }
        @Test
        void testRealizarBackup_OK2() {

                ReflectionTestUtils.setField(
                        backupService,
                        "datasourceUrl",
                        "jdbc:mysql://localhost:3306/inventario"
                );

                ReflectionTestUtils.setField(
                        backupService,
                        "dbUser",
                        "root"
                );

                ReflectionTestUtils.setField(
                        backupService,
                        "backupPath",
                        "C:/temp/"
                );

                ReflectionTestUtils.setField(
                        backupService,
                        "mysqlDumpPath",
                        "echo"
                );
                }
        @Test
        void testBackupStatusError() {
                ReflectionTestUtils.setField(
                        backupService,
                        "mysqlDumpPath",
                        "ruta_inexistente");

                when(backupLogRepository.save(any(BackupLogs.class)))
                        .thenAnswer(i -> i.getArgument(0));

                backupService.realizarBackup();

                ArgumentCaptor<BackupLogs> captor =
                        ArgumentCaptor.forClass(BackupLogs.class);

                verify(backupLogRepository).save(captor.capture());

                assertEquals("ERROR", captor.getValue().getStatus());
                }
        @Test
        void testBackupInicial_noFalla() {
                        backupService.backupInicial();
                assertTrue(true); // si no lanza excepción, pasa
                }
                @Test
                void testRealizarBackup_noException() {
                        assertDoesNotThrow(() -> backupService.realizarBackup());
                }
}
