package es.pixelvault.tienda.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/** Los datos enviados no son válidos (campos vacíos, rangos incorrectos...). Se traduce en un 400. */
public class PeticionInvalidaException extends ResponseStatusException {

    public PeticionInvalidaException(String mensaje) {
        super(HttpStatus.BAD_REQUEST, mensaje);
    }
}
