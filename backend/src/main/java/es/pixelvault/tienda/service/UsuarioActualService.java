package es.pixelvault.tienda.service;

import es.pixelvault.tienda.exception.NoAutenticadoException;
import es.pixelvault.tienda.model.Usuario;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/*
 * ÚNICO punto del código que sabe quién es el usuario de la petición.
 * El resto de servicios (carrito, pedidos...) lo llaman en vez de recibir ids por la ruta,
 * así un cliente nunca puede operar sobre los datos de otro.
 *
 * La identidad sale del token ya verificado por JwtAuthenticationFilter (Persona A),
 * que deja el correo del usuario en el SecurityContext.
 */

@Service
public class UsuarioActualService {

    private final UsuarioService usuarioService;

    public UsuarioActualService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public Usuario obtenerUsuario() {
        Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacion == null || !autenticacion.isAuthenticated()
                || autenticacion instanceof AnonymousAuthenticationToken) {
            throw new NoAutenticadoException("Debes iniciar sesión para realizar esta operación");
        }
        return usuarioService.buscarPorEmail(autenticacion.getName())
                .orElseThrow(() -> new NoAutenticadoException("Tu sesión ya no es válida. Inicia sesión de nuevo"));
    }
}