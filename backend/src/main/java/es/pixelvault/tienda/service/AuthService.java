package es.pixelvault.tienda.service;

import es.pixelvault.tienda.dto.LoginDto;
import es.pixelvault.tienda.dto.RegistroDto;
import es.pixelvault.tienda.dto.TokenDto;
import es.pixelvault.tienda.exception.NoAutenticadoException;
import es.pixelvault.tienda.exception.PeticionInvalidaException;
import es.pixelvault.tienda.model.Usuario;
import es.pixelvault.tienda.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/** Registro e inicio de sesión. Ambos devuelven un JWT. */
@Service
public class AuthService {

    private static final int PASSWORD_MINIMA = 8;

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UsuarioService usuarioService, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public TokenDto registrar(RegistroDto dto) {
        if (dto == null || dto.getEmail() == null || !dto.getEmail().contains("@") || dto.getEmail().isBlank()) {
            throw new PeticionInvalidaException("Indica un correo electrónico válido");
        }
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new PeticionInvalidaException("El nombre es obligatorio");
        }
        if (dto.getPassword() == null || dto.getPassword().length() < PASSWORD_MINIMA) {
            throw new PeticionInvalidaException("La contraseña debe tener al menos " + PASSWORD_MINIMA + " caracteres");
        }
        // UsuarioService asigna el rol (ADMIN si el correo está en tienda.admins) y rechaza duplicados (409).
        Usuario usuario = usuarioService.crear(dto.getEmail(), dto.getNombre(), passwordEncoder.encode(dto.getPassword()));
        return crearToken(usuario);
    }

    public TokenDto login(LoginDto dto) {
        if (dto == null || dto.getEmail() == null || dto.getPassword() == null) {
            throw new PeticionInvalidaException("Indica el correo y la contraseña");
        }
        // Mismo mensaje para "no existe" y "contraseña incorrecta": no se revela qué correos están registrados.
        Usuario usuario = usuarioService.buscarPorEmail(dto.getEmail())
                .filter(u -> passwordEncoder.matches(dto.getPassword(), u.getPasswordHash()))
                .orElseThrow(() -> new NoAutenticadoException("Correo o contraseña incorrectos"));
        return crearToken(usuario);
    }

    private TokenDto crearToken(Usuario usuario) {
        return new TokenDto(jwtService.generarToken(usuario), usuario.getEmail(),
                usuario.getRol().name(), jwtService.getExpiracionMinutos());
    }
}
