package com.caso3.inventario.controller;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.caso3.inventario.dto.LogDTO;
import com.caso3.inventario.dto.StockResponse;
import com.caso3.inventario.model.Categoria;
import com.caso3.inventario.model.Producto;
import com.caso3.inventario.service.InvService;
import com.caso3.inventario.service.LogService;

@RestController
@RequestMapping("/api/inventario")
public class InvController {

    @Autowired
    private InvService service;
    @Autowired
    private LogService logservice;

    @PostMapping("/categorias")
    public ResponseEntity<Categoria> crear(@RequestBody Categoria categoria) {
        Categoria creada = service.crearCategoria(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<Categoria>> listarCategoria() {
        return ResponseEntity.ok(service.listarCategorias());
    }

    @GetMapping("/categorias/{id}")
    public ResponseEntity<Categoria> obtenerCategoriaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerCategoriaPorId(id));
    }

    @PutMapping("/categorias/{id}")
    public ResponseEntity<Categoria> actualizarCategoria(@PathVariable Long id, @RequestBody Categoria categoria) {
        return ResponseEntity.ok(service.actualizarCategoria(id, categoria));
    }

    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        service.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/logs")
    public ResponseEntity<List<LogDTO>> listarLogs() {
        return ResponseEntity.ok(logservice.listar());
    }
    @PostMapping("/Logs")
    public ResponseEntity<LogDTO> guardarLogs(@RequestBody LogDTO log) {
        return ResponseEntity.ok(logservice.guardar(log));
    }

    @GetMapping("/productos")
    public List<Producto> getProductos() {
        return service.readAllProd();
    }

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

    @GetMapping("/productos/{id}")
    public Optional<Producto>getProducto(@PathVariable Long id) {
        return service.readByid(id);
    }
    

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
    @GetMapping("/stock/{id}")
    public ResponseEntity<StockResponse> consultarStock(@PathVariable Long id) {
        return ResponseEntity.ok(service.consultarStock(id));
    }
    @DeleteMapping("/deleteprod/{id}")
    public ResponseEntity<?> deleteProducto(@PathVariable Long id) {
        try {
            service.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Producto eliminado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
        }
    }

    @GetMapping("/productos/buscar/categoria/{idCategoria}")
    public ResponseEntity<List<Producto>> getProductosOrdenadosPorCategoria(@PathVariable Long idCategoria) {
        Categoria categoria = service.obtenerCategoriaPorId(idCategoria);
        List<Producto> productos = service.buscarPorCategoria(categoria);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/productos/ordenados/categoria/{idCategoria}")
    public ResponseEntity<List<Producto>> getProductoPorCategoria(@PathVariable Long idCategoria) {
        Categoria categoria = service.obtenerCategoriaPorId(idCategoria);
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
    @GetMapping("/alerta/{id}")
    public ResponseEntity<?> verificarBajoStock(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(
                    service.verificarBajoStock(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Producto no encontrado");
        }
    }
    @GetMapping("/disponibilidad/{id}/{cantidad}")
    public ResponseEntity<?> verificarDisponibilidad(
            @PathVariable Long id,
            @PathVariable Integer cantidad) {

        try {
            return ResponseEntity.ok(
                    service.verificarDisponibilidad(id, cantidad)
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Producto no encontrado");
        }
    }
    @PostMapping("/ingresar-stock/{idProducto}/{idProveedor}/{cantidad}")
    public ResponseEntity<String> ingresarStock(
            @PathVariable Long idProducto,
            @PathVariable Long idProveedor,
            @PathVariable Integer cantidad) {

        service.ingresarStock(idProducto, idProveedor, cantidad);

        return ResponseEntity.ok("Stock actualizado y log registrado");
    }

    @PatchMapping("/productos/{id}/deshabilitar")
    public ResponseEntity<?> deshabilitarProducto(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.deshabilitarProducto(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
        }
    }

    @PatchMapping("/productos/{id}/habilitar")
    public ResponseEntity<?> habilitarProducto(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.habilitarProducto(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
        }
    }

    @PatchMapping("/productos/{id}/descuento/{porcentaje}")
    public ResponseEntity<?> aplicarDescuentoPorId(
            @PathVariable Long id,
            @PathVariable double porcentaje) {
        try {
            return ResponseEntity.ok(service.aplicarDescuentoPorId(id, porcentaje));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
        }
    }

    @DeleteMapping("/productos/{id}/descuento")
    public ResponseEntity<?> quitarDescuento(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.quitarDescuento(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
        }
    }

    @PatchMapping("/categorias/{idCategoria}/descuento/{porcentaje}")
    public ResponseEntity<?> aplicarDescuentoPorCategoria(
            @PathVariable Long idCategoria,
            @PathVariable double porcentaje) {
        try {
            return ResponseEntity.ok(service.aplicarDescuentoPorCategoria(idCategoria, porcentaje));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
