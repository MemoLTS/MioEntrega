package com.pruebas.unitarias.service;

import com.caso3.inventario.model.BackupLogs;
import com.caso3.inventario.repository.BackupLogRepository;
import com.caso3.inventario.service.BackupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BackupServiceTest {

        @InjectMocks
        private BackupService backupService;

        @Mock
        private BackupLogRepository backupLogRepository;

        @Mock
        private Process process;

        @BeforeEach
        void setUp() {

        ReflectionTestUtils.setField(
                backupService,
                "datasourceUrl",
                "jdbc:mysql://localhost:3306/inventario");

        ReflectionTestUtils.setField(
                backupService,
                "dbUser",
                "root");

        ReflectionTestUtils.setField(
                backupService,
                "backupPath",
                "target/backup/");

        ReflectionTestUtils.setField(
                backupService,
                "mysqlDumpPath",
                "C:/xampp/mysql/bin/mysqldump.exe");
        }

        @Test
        void testExtractDbName() throws Exception {

                Method method = BackupService.class.getDeclaredMethod(
                        "extractDbName",
                        String.class);

                method.setAccessible(true);

                String db = (String) method.invoke(
                        backupService,
                        "jdbc:mysql://localhost:3306/inventario");

                assertEquals("inventario", db);
        }

        @Test
        void testStartProcess() throws Exception {

                ProcessBuilder pb = mock(ProcessBuilder.class);

                when(pb.start()).thenReturn(process);

                Process p = backupService.startProcess(pb);

                assertNotNull(p);
        }

        @Test
        void testCrearDirectorio() {

                File dir = new File("target/pruebaBackup");

                if (dir.exists()) {
                dir.delete();
                }

                ReflectionTestUtils.setField(
                        backupService,
                        "backupPath",
                        "target/pruebaBackup/");

                ReflectionTestUtils.setField(
                        backupService,
                        "mysqlDumpPath",
                        "ruta_inexistente");

                backupService.realizarBackup();

                assertTrue(dir.exists());
        }

        @Test
        void testBackupStatusError() {

        ReflectionTestUtils.setField(
                backupService,
                "mysqlDumpPath",
                "ruta_inexistente");

        when(backupLogRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        backupService.realizarBackup();

        ArgumentCaptor<BackupLogs> captor =
                ArgumentCaptor.forClass(BackupLogs.class);

        verify(backupLogRepository)
                .save(captor.capture());

        assertEquals(
                "ERROR",
                captor.getValue().getStatus());
        }

        @Test
        void testBackupStatusOk() throws Exception {

                BackupService spy = Mockito.spy(backupService);

                doReturn(process)
                        .when(spy)
                        .startProcess(any(ProcessBuilder.class));

                when(process.waitFor())
                        .thenReturn(0);

                spy.realizarBackup();

                ArgumentCaptor<BackupLogs> captor =
                        ArgumentCaptor.forClass(BackupLogs.class);

                verify(backupLogRepository)
                        .save(captor.capture());

                assertEquals(
                        "OK",
                        captor.getValue().getStatus());
        }

        @Test
        void testBackupException() throws Exception {

                BackupService spy = Mockito.spy(backupService);

                doThrow(new IOException("Error"))
                        .when(spy)
                        .startProcess(any(ProcessBuilder.class));

                spy.realizarBackup();

                ArgumentCaptor<BackupLogs> captor =
                        ArgumentCaptor.forClass(BackupLogs.class);

                verify(backupLogRepository)
                        .save(captor.capture());

                assertEquals(
                        "ERROR",
                        captor.getValue().getStatus());
        }

        @Test
        void testBackupInicial() {
                assertDoesNotThrow(() -> backupService.backupInicial());
        }

        @Test
        void testRealizarBackupNoException() {
                assertDoesNotThrow(() -> backupService.realizarBackup());
        }
}
