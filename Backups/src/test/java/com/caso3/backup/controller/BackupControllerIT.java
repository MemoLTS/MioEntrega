package com.caso3.backup.controller;

import com.caso3.backup.BackupApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BackupApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BackupControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldListBackupsSuccessfully() throws Exception {
        mockMvc.perform(get("/api/v1/backup"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateBackupSuccessfully() throws Exception {
        mockMvc.perform(post("/api/v1/backup"))
                .andExpect(status().isOk());
    }
}

