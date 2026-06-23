package com.caso3.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.caso3.inventario.model.BackupLogs;

public interface BackupLogRepository extends JpaRepository<BackupLogs, Long> {
}