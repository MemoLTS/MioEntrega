package com.caso3.backup.controller;


import com.caso3.backup.model.Backup;
import com.caso3.backup.model.BackupConfig;
import com.caso3.backup.service.BackupConfigService;
import com.caso3.backup.service.BackupService;


import org.junit.jupiter.api.Test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDateTime;
import java.util.List;


import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.http.MediaType.APPLICATION_JSON;



@SuppressWarnings("unused")
@WebMvcTest(BackupController.class)
class BackupControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @SuppressWarnings("removal")
    @MockBean
    private BackupService backupService;


    @SuppressWarnings("removal")
    @MockBean
    private BackupConfigService configService;

    @Test
    void listarBackups() throws Exception {
        Backup backup = new Backup();
        backup.setId(1L);
        backup.setTipo("INICIAL");
        backup.setFechaCreacion(LocalDateTime.now());
        when(backupService.listar())
                .thenReturn(List.of(backup));
        mockMvc.perform(
                get("/api/v1/backups")
        )
        .andExpect(status().isOk());
    }


    @Test
    void crearBackupManual() throws Exception {
        Backup backup = new Backup();

        backup.setId(1L);
        backup.setTipo("MANUAL");
        when(
            backupService.crearBackup("MANUAL")
        )
        .thenReturn(backup);
        mockMvc.perform(
                post("/api/v1/backups")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.tipo").value("MANUAL"));
    }

    @Test
    void obtenerUltimoBackup() throws Exception {
        Backup backup = new Backup();

        backup.setId(1L);
        backup.setTipo("INICIAL");
        when(
            backupService.obtenerUltimo()
        )
        .thenReturn(backup);
        mockMvc.perform(
                get("/api/v1/backups/ultimo")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.tipo").value("INICIAL"));
    }


    @Test
    void testActivarBackup() throws Exception {

        BackupConfig config = new BackupConfig();
        config.setActivo(true);

        when(configService.activar())
                .thenReturn(config);

        mockMvc.perform(put("/api/v1/backups/activar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo")
                        .value(true));

        verify(configService)
                .activar();
    }

    @Test
    void testDesactivarBackup() throws Exception {

        BackupConfig config = new BackupConfig();
        config.setActivo(false);

        when(configService.desactivar())
                .thenReturn(config);

        mockMvc.perform(put("/api/v1/backups/desactivar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo")
                        .value(false));

        verify(configService)
                .desactivar();
    }
    @Test
    void testObtenerConfig() throws Exception {

        BackupConfig config = new BackupConfig();
        config.setActivo(true);

        when(configService.obtener())
                .thenReturn(config);

        mockMvc.perform(get("/api/v1/backups/config"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo")
                        .value(true));

        verify(configService)
                .obtener();
    }
}