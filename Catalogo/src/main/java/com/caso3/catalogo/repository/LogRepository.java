package com.caso3.catalogo.repository;
import com.caso3.catalogo.dto.LogDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<LogDTO, Long> {

}