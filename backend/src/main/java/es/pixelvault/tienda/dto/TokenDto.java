package es.pixelvault.tienda.dto;

/** Respuesta de login y registro: el JWT que el cliente enviará en "Authorization: Bearer ...". */
public class TokenDto {

    private String token;
    private String tipo = "Bearer";
    private String email;
    private String rol;
    private long expiraEnMinutos;

    public TokenDto() {
    }

    public TokenDto(String token, String email, String rol, long expiraEnMinutos) {
        this.token = token;
        this.email = email;
        this.rol = rol;
        this.expiraEnMinutos = expiraEnMinutos;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public long getExpiraEnMinutos() { return expiraEnMinutos; }
    public void setExpiraEnMinutos(long expiraEnMinutos) { this.expiraEnMinutos = expiraEnMinutos; }
}
