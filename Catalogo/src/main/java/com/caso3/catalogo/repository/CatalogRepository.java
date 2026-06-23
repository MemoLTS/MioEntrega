package com.caso3.catalogo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.caso3.catalogo.dto.ProductoDTO;

@Repository
public interface CatalogRepository extends JpaRepository<ProductoDTO, Long> {
}