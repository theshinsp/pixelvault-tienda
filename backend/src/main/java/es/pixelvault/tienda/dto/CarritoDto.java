package es.pixelvault.tienda.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** Vista del carrito. Todos los importes los calcula el servidor. */
public class CarritoDto {

    private List<LineaCarritoDto> lineas = new ArrayList<>();
    private int numeroArticulos;
    private BigDecimal subtotal;
    private BigDecimal gastosEnvio;
    private BigDecimal total;
    private BigDecimal faltaParaEnvioGratis;
    private List<String> avisos = new ArrayList<>();
    private boolean confirmable;

    public CarritoDto() {
    }

    public List<LineaCarritoDto> getLineas() { return lineas; }
    public void setLineas(List<LineaCarritoDto> lineas) { this.lineas = lineas; }
    public int getNumeroArticulos() { return numeroArticulos; }
    public void setNumeroArticulos(int numeroArticulos) { this.numeroArticulos = numeroArticulos; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getGastosEnvio() { return gastosEnvio; }
    public void setGastosEnvio(BigDecimal gastosEnvio) { this.gastosEnvio = gastosEnvio; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public BigDecimal getFaltaParaEnvioGratis() { return faltaParaEnvioGratis; }
    public void setFaltaParaEnvioGratis(BigDecimal faltaParaEnvioGratis) { this.faltaParaEnvioGratis = faltaParaEnvioGratis; }
    public List<String> getAvisos() { return avisos; }
    public void setAvisos(List<String> avisos) { this.avisos = avisos; }
    public boolean isConfirmable() { return confirmable; }
    public void setConfirmable(boolean confirmable) { this.confirmable = confirmable; }
}
