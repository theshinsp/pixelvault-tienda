package es.pixelvault.tienda.service;

import es.pixelvault.tienda.dto.UsuarioActualizarDto;
import es.pixelvault.tienda.dto.UsuarioDto;
import es.pixelvault.tienda.exception.ConflictoException;
import es.pixelvault.tienda.exception.PeticionInvalidaException;
import es.pixelvault.tienda.exception.RecursoNoEncontradoException;
import es.pixelvault.tienda.model.Rol;
import es.pixelvault.tienda.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Almacén de usuarios en memoria y gestión de roles.
 * <p>
 * Contrato con AuthService (Persona A): crear(email, nombre, passwordHash) asigna el rol
 * (ADMIN si el correo está en tienda.admins; CLIENTE en otro caso) y buscarPorEmail(email).
 */
@Service
public class UsuarioService {

    private final Map<Long, Usuario> usuarios = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong(0);
    private final Set<String> emailsAdmin;

    public UsuarioService(@Value("${tienda.admins:}") String admins) {
        this.emailsAdmin = Arrays.stream(admins.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    public synchronized Usuario crear(String email, String nombre, String passwordHash) {
        String emailNormalizado = normalizarEmail(email);
        if (buscarPorEmail(emailNormalizado).isPresent()) {
            throw new ConflictoException("Ya existe un usuario registrado con ese correo");
        }
        Usuario usuario = new Usuario();
        usuario.setId(secuencia.incrementAndGet());
        usuario.setEmail(emailNormalizado);
        usuario.setNombre(nombre.trim());
        usuario.setPasswordHash(passwordHash);
        usuario.setRol(emailsAdmin.contains(emailNormalizado) ? Rol.ADMIN : Rol.CLIENTE);
        usuario.setFechaAlta(LocalDateTime.now());
        usuarios.put(usuario.getId(), usuario);
        return usuario;
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }
        String buscado = normalizarEmail(email);
        return usuarios.values().stream()
                .filter(u -> u.getEmail().equals(buscado))
                .findFirst();
    }

    public Usuario obtenerEntidad(Long id) {
        Usuario usuario = id == null ? null : usuarios.get(id);
        if (usuario == null) {
            throw new RecursoNoEncontradoException("No existe ningún usuario con id " + id);
        }
        return usuario;
    }

    public List<UsuarioDto> listar() {
        return usuarios.values().stream()
                .sorted(Comparator.comparing(Usuario::getId))
                .map(this::aDto)
                .toList();
    }

    public UsuarioDto actualizarPerfil(Usuario usuario, UsuarioActualizarDto dto) {
        if (dto == null || dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new PeticionInvalidaException("El nombre es obligatorio");
        }
        usuario.setNombre(dto.getNombre().trim());
        usuario.setDireccionEnvio(dto.getDireccionEnvio() == null ? null : dto.getDireccionEnvio().trim());
        return aDto(usuario);
    }

    public UsuarioDto cambiarRol(Long id, Rol nuevoRol) {
        if (nuevoRol == null) {
            throw new PeticionInvalidaException("Indica el rol (CLIENTE o ADMIN)");
        }
        Usuario usuario = obtenerEntidad(id);
        usuario.setRol(nuevoRol);
        return aDto(usuario);
    }

    public UsuarioDto aDto(Usuario usuario) {
        UsuarioDto dto = new UsuarioDto();
        dto.setId(usuario.getId());
        dto.setEmail(usuario.getEmail());
        dto.setNombre(usuario.getNombre());
        dto.setDireccionEnvio(usuario.getDireccionEnvio());
        dto.setRol(usuario.getRol());
        dto.setFechaAlta(usuario.getFechaAlta());
        return dto;
    }

    private String normalizarEmail(String email) {
        return email.trim().toLowerCase();
    }
}
