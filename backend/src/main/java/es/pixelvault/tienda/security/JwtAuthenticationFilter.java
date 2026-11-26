package es.pixelvault.tienda.security;

import es.pixelvault.tienda.model.Usuario;
import es.pixelvault.tienda.service.UsuarioService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Se ejecuta en TODAS las peticiones. Si trae "Authorization: Bearer <token>" válido, deja al usuario
 * autenticado en el SecurityContext con su rol actual. Si no hay token o no es válido, no autentica
 * y es SecurityConfig quien responde 401 (ruta protegida) o deja pasar (rutas públicas).
 *
 * El rol se lee del usuario guardado y no del token, para que un cambio de rol (PUT /rol) surta
 * efecto en la siguiente petición sin esperar a que el token caduque.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String PREFIJO = "Bearer ";

    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    public JwtAuthenticationFilter(JwtService jwtService, UsuarioService usuarioService) {
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String cabecera = request.getHeader("Authorization");

        if (cabecera != null && cabecera.startsWith(PREFIJO)) {
            String token = cabecera.substring(PREFIJO.length()).trim();
            try {
                String email = jwtService.obtenerEmail(token);
                Optional<Usuario> usuario = usuarioService.buscarPorEmail(email);
                if (usuario.isPresent()) {
                    var autoridad = new SimpleGrantedAuthority("ROLE_" + usuario.get().getRol().name());
                    var autenticacion = new UsernamePasswordAuthenticationToken(
                            usuario.get().getEmail(), null, List.of(autoridad));
                    SecurityContextHolder.getContext().setAuthentication(autenticacion);
                }
            } catch (JwtException | IllegalArgumentException e) {
                // Token manipulado, mal formado o caducado: se ignora y la petición queda sin autenticar.
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(request, response);
    }
}
