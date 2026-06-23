package com.caso3.inventario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.caso3.inventario.model.Categoria;
import com.caso3.inventario.model.Producto;
import com.caso3.inventario.repository.ProductoRepository;

@Service
public class InvService {
    @Autowired
    private ProductoRepository Repository;

    public List<Producto> readAllProd() {
        return Repository.findAll();
    }
    public Optional<Producto> readByid(Long id){
        return Repository.findById(id);
    }

    public List<Producto> readByCategoria(Categoria categoria) {
        return Repository.findByCategoria(categoria);
    }

    public Producto updateProducto(Long id, Producto datosNuevos) {
        Producto existente = Repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        existente.setNombre(datosNuevos.getNombre());
        existente.setPrecio(datosNuevos.getPrecio());
        existente.setStock(datosNuevos.getStock());
        existente.setCategoria(datosNuevos.getCategoria());
        return Repository.save(existente);
    }

    public Producto register(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("Producto no puede ser nulo");
        }
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        if (producto.getNombre().length() < 3) {
            throw new IllegalArgumentException("El nombre debe tener al menos 3 caracteres");
        }
        if (producto.getPrecio() == null || producto.getPrecio() < 0) {
            throw new IllegalArgumentException("El precio debe ser mayor o igual a 0");
        }
        if (producto.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        if (producto.getCategoria() == null) {
            throw new IllegalArgumentException("La categoría es obligatoria");
        }
        return Repository.save(producto);
    }

    public Producto updateStock(Long id, int cantidad) {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        Producto producto = Repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        producto.setStock(cantidad);
        return Repository.save(producto);
    }

    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        if (!Repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado");
        }
        Repository.deleteById(id);
    }
    public List<Producto> obtenerPorCategoria(String categoria) {
        return Repository.findAll()
                .stream()
                .filter(p -> p.getCategoria() != null &&
                        p.getCategoria().toString()
                                .equalsIgnoreCase(categoria))
                .toList();
    }

    public List<Producto> buscarPorCategoria(Categoria categoria) {
        return Repository.findByCategoria(categoria);
    }
}