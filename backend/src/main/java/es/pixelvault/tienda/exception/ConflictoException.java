package es.pixelvault.tienda.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/** La operación choca con el estado actual (duplicados, stock, transición no válida...). Se traduce en un 409. */
public class ConflictoException extends ResponseStatusException {

    public ConflictoException(String mensaje) {
        super(HttpStatus.CONFLICT, mensaje);
    }
}
