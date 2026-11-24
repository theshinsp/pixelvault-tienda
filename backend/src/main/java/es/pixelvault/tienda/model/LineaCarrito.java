package es.pixelvault.tienda.model;

public class LineaCarrito {

    private Videojuego videojuego;
    private int cantidad;

    public LineaCarrito() {
    }

    public LineaCarrito(Videojuego videojuego, int cantidad) {
        this.videojuego = videojuego;
        this.cantidad = cantidad;
    }

    public Videojuego getVideojuego() {
        return videojuego;
    }

    public void setVideojuego(Videojuego videojuego) {
        this.videojuego = videojuego;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}