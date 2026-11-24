package es.pixelvault.tienda.dto;

import es.pixelvault.tienda.model.Direccion;
import es.pixelvault.tienda.model.OrdenProducto;
import es.pixelvault.tienda.model.Plataforma;

import java.math.BigDecimal;

/** Agrupa todos los parámetros opcionales de GET /api/productos. Para tenerlo todos controlados en un mismo lugar */
public class VideojuegoFiltroDto {

    private Long categoriaId;
    private String q;
    private BigDecimal precioMin;
    private BigDecimal precioMax;
    private Boolean soloDisponibles;
    private OrdenProducto orden;
    private Direccion direccion;
    private Plataforma plataforma;

    public VideojuegoFiltroDto() {
    }

    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }
    public String getQ() { return q; }
    public void setQ(String q) { this.q = q; }
    public BigDecimal getPrecioMin() { return precioMin; }
    public void setPrecioMin(BigDecimal precioMin) { this.precioMin = precioMin; }
    public BigDecimal getPrecioMax() { return precioMax; }
    public void setPrecioMax(BigDecimal precioMax) { this.precioMax = precioMax; }
    public Boolean getSoloDisponibles() { return soloDisponibles; }
    public void setSoloDisponibles(Boolean soloDisponibles) { this.soloDisponibles = soloDisponibles; }
    public OrdenProducto getOrden() { return orden; }
    public void setOrden(OrdenProducto orden) { this.orden = orden; }
    public Direccion getDireccion() { return direccion; }
    public void setDireccion(Direccion direccion) { this.direccion = direccion; }
    public Plataforma getPlataforma() { return plataforma; }
    public void setPlataforma(Plataforma plataforma) { this.plataforma = plataforma; }
}
