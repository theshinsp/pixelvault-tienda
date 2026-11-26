package es.pixelvault.tienda.dto;

import java.math.BigDecimal;

public class LineaCarritoDto {

    private Long productoId;
    private String sku;
    private String nombre;
    private String imagenUrl;
    private BigDecimal precioUnitario;
    private int cantidad;
    private BigDecimal subtotal;
    private int stockDisponible;

    public LineaCarritoDto() {
    }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public int getStockDisponible() { return stockDisponible; }
    public void setStockDisponible(int stockDisponible) { this.stockDisponible = stockDisponible; }
}
