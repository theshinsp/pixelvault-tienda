package es.pixelvault.tienda.dto;

import es.pixelvault.tienda.model.Rol;

public class CambioRolDto {

    private Rol rol;

    public CambioRolDto() {
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}