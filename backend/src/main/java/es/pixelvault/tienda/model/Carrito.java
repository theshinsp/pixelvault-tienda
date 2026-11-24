package es.pixelvault.tienda.model;

import java.util.ArrayList;
import java.util.List;

/*
 * Carrito de un cliente (uno por usuario). Los importes no se guardan: se calculan siempre.
 */
public class Carrito {

    private Usuario usuario;
    private List<LineaCarrito> lineas = new ArrayList<>();

    public Carrito() {
    }

    public Carrito(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<LineaCarrito> getLineas() {
        return lineas;
    }

    public void setLineas(List<LineaCarrito> lineas) {
        this.lineas = lineas;
    }
}