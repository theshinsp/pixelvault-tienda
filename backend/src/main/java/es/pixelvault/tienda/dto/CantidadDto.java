package es.pixelvault.tienda.dto;

/** Entrada de PUT /api/carrito/lineas/{productoId}: { "cantidad": 3 }. */
public class CantidadDto {

    private Integer cantidad;

    public CantidadDto() {
    }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}
