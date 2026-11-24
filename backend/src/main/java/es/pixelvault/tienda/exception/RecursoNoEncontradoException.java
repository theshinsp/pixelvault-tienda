package es.pixelvault.tienda.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/** El recurso pedido no existe (o no es visible para el usuario). Se traduce en un 404. */
public class RecursoNoEncontradoException extends ResponseStatusException {

    public RecursoNoEncontradoException(String mensaje) {
        super(HttpStatus.NOT_FOUND, mensaje);
    }
}
