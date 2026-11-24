package es.pixelvault.tienda.dto;

/** Esto es la entrada para crear o modificar un género. El cliente nunca enviaría el id. */
public class GeneroCrearDto {

    private String nombre;
    private String descripcion;

    public GeneroCrearDto() {
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
