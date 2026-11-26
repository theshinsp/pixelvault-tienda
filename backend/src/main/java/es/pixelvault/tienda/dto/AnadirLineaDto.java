package es.pixelvault.tienda.dto;

/** Entrada de POST /api/carrito/lineas: { "productoId": 7, "cantidad": 2 }. */
public class AnadirLineaDto {

    private Long productoId;
    private Integer cantidad;

    public AnadirLineaDto() {
    }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}
