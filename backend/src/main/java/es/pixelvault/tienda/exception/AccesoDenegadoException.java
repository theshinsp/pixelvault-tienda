package es.pixelvault.tienda.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/** El usuario está autenticado pero no tiene el rol necesario. Se traduce en un 403. */
public class AccesoDenegadoException extends ResponseStatusException {

    public AccesoDenegadoException(String mensaje) {
        super(HttpStatus.FORBIDDEN, mensaje);
    }
}
