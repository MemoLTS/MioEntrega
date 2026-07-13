package com.caso3.catalogo.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caso3.catalogo.client.ProductClient;
import com.caso3.catalogo.dto.CategoriaDTO;
import com.caso3.catalogo.dto.ProductoDTO;
import com.caso3.catalogo.repository.CatalogRepository;
import com.caso3.catalogo.repository.CategoriaCatalogRepository;

/**
 * Catalogo mantiene una copia local (BD propia) de los productos de
 * Inventario. Inventario es siempre la fuente de verdad: en cada request
 * de lectura, se trae el estado actual desde Inventario y se compara
 * contra lo guardado localmente. Si algo no coincide (precio, stock,
 * nombre, categoría, descuento o si fue activado/desactivado), se
 * actualiza la copia local. Los productos que ya no existen en Inventario
 * se eliminan del catálogo local. Los productos inactivos nunca se
 * devuelven al cliente del catálogo.
 */
@Service
public class CatalogService {

    @Autowired
    private ProductClient productClient;

    @Autowired
    private CatalogRepository catalogRepository;

    @Autowired
    private CategoriaCatalogRepository categoriaCatalogRepository;

    public CatalogService(ProductClient productClient,
                           CatalogRepository catalogRepository,
                           CategoriaCatalogRepository categoriaCatalogRepository) {
        this.productClient = productClient;
        this.catalogRepository = catalogRepository;
        this.categoriaCatalogRepository = categoriaCatalogRepository;
    }

    /** Trae el estado real de Inventario y actualiza la copia local si hace falta. */
    private void sincronizarConInventario() {
        List<ProductoDTO> productosInventario = productClient.obtenerProductos();

        List<ProductoDTO> productosLocales = catalogRepository.findAll();
        Map<Long, ProductoDTO> localesPorId = productosLocales.stream()
                .collect(Collectors.toMap(ProductoDTO::getId, p -> p));

        for (ProductoDTO remoto : productosInventario) {
            remoto.setCategoria(sincronizarCategoria(remoto.getCategoria()));

            ProductoDTO local = localesPorId.get(remoto.getId());
            if (local == null || !coincide(local, remoto)) {
                catalogRepository.save(remoto);
            }
        }

        // Productos que ya no existen en Inventario: se quitan del catálogo local
        Set<Long> idsVigentes = productosInventario.stream()
                .map(ProductoDTO::getId)
                .collect(Collectors.toSet());
        List<Long> idsObsoletos = productosLocales.stream()
                .map(ProductoDTO::getId)
                .filter(id -> !idsVigentes.contains(id))
                .toList();
        if (!idsObsoletos.isEmpty()) {
            catalogRepository.deleteAllById(idsObsoletos);
        }
    }

    private CategoriaDTO sincronizarCategoria(CategoriaDTO remota) {
        if (remota == null || remota.getId() == null) {
            return remota;
        }
        CategoriaDTO local = categoriaCatalogRepository.findById(remota.getId()).orElse(null);
        boolean coincide = local != null
                && Objects.equals(local.getNombre(), remota.getNombre())
                && Objects.equals(local.getDescripcion(), remota.getDescripcion());
        if (!coincide) {
            return categoriaCatalogRepository.save(remota);
        }
        return local;
    }

    private boolean coincide(ProductoDTO local, ProductoDTO remoto) {
        Long idCategoriaLocal = local.getCategoria() != null ? local.getCategoria().getId() : null;
        Long idCategoriaRemoto = remoto.getCategoria() != null ? remoto.getCategoria().getId() : null;

        return Objects.equals(local.getNombre(), remoto.getNombre())
                && Objects.equals(local.getPrecio(), remoto.getPrecio())
                && local.getStock() == remoto.getStock()
                && local.isActivo() == remoto.isActivo()
                && Double.compare(local.getDescuentoPorcentaje(), remoto.getDescuentoPorcentaje()) == 0
                && Objects.equals(idCategoriaLocal, idCategoriaRemoto);
    }

    /** Catálogo completo, siempre sincronizado y sin productos inactivos. */
    public List<ProductoDTO> verCatalogo() {
        sincronizarConInventario();
        return catalogRepository.findAll()
                .stream()
                .filter(ProductoDTO::isActivo)
                .toList();
    }

    /** Se mantiene como alias explícito por compatibilidad con el endpoint /ver/disponibles. */
    public List<ProductoDTO> verCatalogoDisponible() {
        return verCatalogo();
    }

    public List<ProductoDTO> obtenerPorCategoria(Long idCategoria) {
        sincronizarConInventario();
        return catalogRepository.findAll()
                .stream()
                .filter(ProductoDTO::isActivo)
                .filter(p -> p.getCategoria() != null && idCategoria.equals(p.getCategoria().getId()))
                .toList();
    }

    public List<ProductoDTO> obtenerPorNombre(String nombre) {
        sincronizarConInventario();
        return catalogRepository.findAll()
                .stream()
                .filter(ProductoDTO::isActivo)
                .filter(p -> p.getNombre() != null
                        && p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .toList();
    }

    /** Productos activos cuya categoría coincide con el nombre indicado (ignora mayúsculas/minúsculas). */
    public List<ProductoDTO> obtenerPorNombreCategoria(String nombreCategoria) {
        sincronizarConInventario();
        return catalogRepository.findAll()
                .stream()
                .filter(ProductoDTO::isActivo)
                .filter(p -> p.getCategoria() != null
                        && p.getCategoria().getNombre() != null
                        && p.getCategoria().getNombre().equalsIgnoreCase(nombreCategoria))
                .toList();
    }

}
