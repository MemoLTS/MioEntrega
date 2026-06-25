package com.caso3.inventario.dto;

public class StockResponse {

    private Long idProducto;
    private String nombre;
    private Integer stock;

    public StockResponse() {
    }

    public StockResponse(Long idProducto, String nombre, Integer stock) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.stock = stock;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
