package com.caso3.backup.repository;


import com.caso3.backup.model.Backup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BackupRepository extends JpaRepository<Backup, Long> {

    boolean existsByTipo(String tipo);
    Backup findTopByOrderByFechaCreacionDesc();


}