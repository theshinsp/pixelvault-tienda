package es.pixelvault.tienda.dto;

/*
 * Ajuste de stock: positivo suma unidades, negativo resta. Ejemplo: { "cantidad": -3 }.
 */
public class StockDto {

    private Integer cantidad;

    public StockDto() {
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
