package es.pixelvault.tienda.model;

import java.time.LocalDateTime;

/*
 * Usuario de la tienda. Con Spring Security + JWT, el campo passwordHash guarda
 * la contraseña cifrada con BCrypt (en el enunciado este campo es "uid").
 * La contraseña nunca se expone en ningún DTO.
 */
public class Usuario {

    private Long id;
    private String email;
    private String passwordHash;
    private String nombre;
    private String direccionEnvio;
    private Rol rol;
    private LocalDateTime fechaAlta;

    public Usuario() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
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

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDateTime fechaAlta) {
        this.fechaAlta = fechaAlta;
    }
}