package es.pixelvault.tienda.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/** Falta el token, no es válido, ha caducado o las credenciales son incorrectas. Se traduce en un 401. */
public class NoAutenticadoException extends ResponseStatusException {

    public NoAutenticadoException(String mensaje) {
        super(HttpStatus.UNAUTHORIZED, mensaje);
    }
}
