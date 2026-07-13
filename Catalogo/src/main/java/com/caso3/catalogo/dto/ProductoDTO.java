package com.caso3.catalogo.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "productos")
public class ProductoDTO {

    /**
     * OJO: este id NO se autogenera. Catalogo es un espejo/cache de Inventario,
     * así que el id de cada producto debe ser exactamente el mismo id que tiene
     * en Inventario (la fuente de verdad). Así la sincronización puede
     * comparar y actualizar por id sin que se desincronicen los identificadores.
     */
    @Id
    private Long id;

    @NotBlank(message = "El nombre del producto no puede estar vacío")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(nullable = false)
    private String nombre;

    @NotNull(message = "El precio no puede ser nulo")
    @Min(value = 0, message = "El precio debe ser mayor o igual a 0")
    @Column(nullable = false)
    private Double precio;

    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private int stock;

    @NotNull(message = "La categoría no puede ser nula")
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private CategoriaDTO categoria;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(nullable = false)
    private double descuentoPorcentaje = 0.0;

    @Transient
    public Double getPrecioFinal() {
        if (precio == null) {
            return null;
        }
        return precio - (precio * (descuentoPorcentaje / 100.0));
    }
}
