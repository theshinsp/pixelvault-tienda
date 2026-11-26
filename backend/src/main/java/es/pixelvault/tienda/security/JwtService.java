package es.pixelvault.tienda.security;

import es.pixelvault.tienda.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Crea y verifica los JWT firmados con HMAC-SHA256 (HS256).
 * El secreto NO está en el repositorio: se lee de tienda.jwt.secret (application-local.properties).
 */
@Service
public class JwtService {

    private static final String EMISOR = "pixelvault-tienda";

    private final SecretKey clave;
    private final long expiracionMinutos;

    public JwtService(@Value("${tienda.jwt.secret:}") String secreto,
                      @Value("${tienda.jwt.expiracion-minutos:120}") long expiracionMinutos) {
        if (secreto == null || secreto.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException(
                    "Falta la propiedad tienda.jwt.secret (mínimo 32 caracteres). "
                            + "Copia application-example.properties como application-local.properties "
                            + "en backend/src/main/resources/ y vuelve a arrancar.");
        }
        this.clave = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
        this.expiracionMinutos = expiracionMinutos;
    }

    public String generarToken(Usuario usuario) {
        Instant ahora = Instant.now();
        return Jwts.builder()
                .issuer(EMISOR)
                .subject(usuario.getEmail())
                .claim("rol", usuario.getRol().name())
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(ahora.plus(expiracionMinutos, ChronoUnit.MINUTES)))
                .signWith(clave)
                .compact();
    }

    /**
     * Verifica firma, emisor y caducidad y devuelve el correo (subject).
     * Lanza io.jsonwebtoken.JwtException si el token no es válido o ha caducado.
     */
    public String obtenerEmail(String token) {
        return Jwts.parser()
                .verifyWith(clave)
                .requireIssuer(EMISOR)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public long getExpiracionMinutos() {
        return expiracionMinutos;
    }
}
