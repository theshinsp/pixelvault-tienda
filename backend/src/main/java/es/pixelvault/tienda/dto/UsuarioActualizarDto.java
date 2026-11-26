package es.pixelvault.tienda.dto;

/*
 * Entrada de PUT /api/usuarios/me.
 */

public class UsuarioActualizarDto {

    private String nombre;
    private String direccionEnvio;

    public UsuarioActualizarDto() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }
}
