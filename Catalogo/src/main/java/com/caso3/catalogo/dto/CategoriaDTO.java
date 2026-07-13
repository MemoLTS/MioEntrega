package com.caso3.catalogo.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa la categoría de un producto tal como la expone la API de Inventario.
 * Antes era un enum; ahora refleja la entidad Categoria (id, nombre, descripcion)
 * para que la deserialización vía Feign coincida con el JSON real de Inventario.
 * Es @Entity porque ProductoDTO (también entidad local de Catalogo) la referencia
 * vía @ManyToOne; Catalogo no crea categorías, solo recibe y guarda lo que llega
 * de Inventario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categorias")
public class CategoriaDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String descripcion;
}
