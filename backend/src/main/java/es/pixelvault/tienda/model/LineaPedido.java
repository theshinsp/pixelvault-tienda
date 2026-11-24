package es.pixelvault.tienda.model;

import java.math.BigDecimal;

/**
 * Línea de un pedido. Guarda una copia del nombre y del precio en el momento de la compra:
 * si el videojuego cambia de precio, los pedidos antiguos no se alteran.
 */
public class LineaPedido {

    private Videojuego videojuego;
    private String nombreProducto;
    private BigDecimal precioUnitario;
    private int cantidad;
    private BigDecimal subtotal;

    public LineaPedido() {
    }

    public Videojuego getVideojuego() {
        return videojuego;
    }

    public void setVideojuego(Videojuego videojuego) {
        this.videojuego = videojuego;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}