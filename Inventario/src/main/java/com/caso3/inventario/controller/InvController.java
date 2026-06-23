package com.caso3.inventario.controller;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.caso3.inventario.model.Categoria;
import com.caso3.inventario.model.Producto;
import com.caso3.inventario.service.InvService;

@RestController
@RequestMapping("/api/inventario")
public class InvController {

    @Autowired
    private InvService service;

    // Obtener todos los productos
    @GetMapping("/productos")
    public List<Producto> getProductos() {
        return service.readAllProd();
    }

    // Agregar producto
    @PostMapping("/addprod")
    public ResponseEntity<?> postProducto(@Valid @RequestBody Producto producto) {
        Producto prod = service.register(producto);
        if (prod != null) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Producto registrado");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error al registrar producto");
    }

    // Buscar producto por ID
    @GetMapping("/productos/{id}")
    public Optional<Producto>getProducto(@PathVariable Long id) {
        return service.readByid(id);
    }
    

    // Actualizar producto
    @PutMapping("/updateprod/{id}")
    public ResponseEntity<?> updateProducto(
            @PathVariable Long id,
            @Valid @RequestBody Producto producto) {

        List<Producto> productos = service.readAllProd();
        for (Producto p : productos) {
            if (p.getId().equals(id)) {
                p.setNombre(producto.getNombre());
                p.setPrecio(producto.getPrecio());
                service.register(p);
                return ResponseEntity.status(HttpStatus.OK).body("Producto actualizado");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
    }

    @PutMapping("/updateprod/{id}/{stock}")
    public ResponseEntity<?> updateStock(
            @PathVariable Long id,
            @PathVariable int stock) {

        try {
            service.updateStock(id, stock);
            return ResponseEntity.ok("Stock actualizado");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Producto no encontrado");
        }
    }

    // Eliminar producto
    @DeleteMapping("/deleteprod/{id}")
    public ResponseEntity<?> deleteProducto(@PathVariable Long id) {
        try {
            service.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Producto eliminado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
        }
    }
    @GetMapping("/productos/buscar/categoria/{categoria}")
    public ResponseEntity<List<Producto>> getProductosOrdenadosPorCategoria(@PathVariable Categoria categoria) {
        List<Producto> productos = service.buscarPorCategoria(categoria);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/productos/ordenados/categoria/{categoria}")
    public ResponseEntity<List<Producto>> getProductoPorCategoria(@PathVariable Categoria categoria) {
        List<Producto> productos = service.buscarPorCategoria(categoria);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/productos/nombre/{nombre}")
    public ResponseEntity<List<Producto>> getPorNombre(
            @PathVariable String nombre){

        List<Producto> productos = service.readAllProd()
                .stream()
                .filter(p -> p.getNombre()
                        .toLowerCase()
                        .contains(nombre.toLowerCase()))
                .toList();

        return ResponseEntity.ok(productos);
}
}
