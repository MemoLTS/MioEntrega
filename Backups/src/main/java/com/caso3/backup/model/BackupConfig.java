package com.caso3.backup.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class BackupConfig {

    @Id
    private Long id = 1L;

    private boolean activo;

    public BackupConfig() {
    }

    public BackupConfig(Long id, boolean activo) {
        this.id = id;
        this.activo = activo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

}