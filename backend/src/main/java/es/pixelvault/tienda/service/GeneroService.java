package es.pixelvault.tienda.service;

import es.pixelvault.tienda.dto.GeneroCrearDto;
import es.pixelvault.tienda.dto.GeneroDto;
import es.pixelvault.tienda.exception.ConflictoException;
import es.pixelvault.tienda.exception.PeticionInvalidaException;
import es.pixelvault.tienda.exception.RecursoNoEncontradoException;
import es.pixelvault.tienda.model.Genero;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Gestión de géneros (categorías) con los datos en memoria.
 *
 * Esta clase NO conoce a VideojuegoService: la regla "no se puede eliminar un género con
 * videojuegos" la orquesta CatalogoService, que depende de los dos. Así se evita la
 * dependencia circular (VideojuegoService -> GeneroService -> VideojuegoService).
 */
@Service
public class GeneroService {

    private final Map<Long, Genero> generos = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong(0);

    /**
     * Datos de ejemplo
     */
    @PostConstruct
    /* Esto se ejecuta después de que el bean ha sido construído e inyectado.
    Documentación: https://blog.joedayz.pe/jakarta-annotations-en-quarkus-gestion-del-ciclo-de-vida-y-anotaciones-personalizadas */
    void cargarDatosEjemplo() {
        insertar("Acción y aventura", "Combate, exploración y mundos abiertos");
        insertar("RPG", "Rol, personajes y decisiones que cambian la historia");
        insertar("Indie", "Joyas de estudios pequeños con mucha personalidad");
        insertar("Estrategia y simulación", "Planificación, gestión y decisiones por turnos");
    }

    public List<GeneroDto> listar() {
        return generos.values().stream()
                .sorted(Comparator.comparing(Genero::getId))
                .map(this::aDto)
                .toList();
    }

    public GeneroDto obtener(Long id) {
        return aDto(obtenerEntidad(id));
    }

    /** Devuelve la entidad o lanza 404. Lo usan otros servicios. */
    public Genero obtenerEntidad(Long id) {
        Genero genero = id == null ? null : generos.get(id);
        if (genero == null) {
            throw new RecursoNoEncontradoException("No existe ninguna categoría con id" + id);
        }
        return genero;
    }

    public boolean existe(Long id) {
        return id != null && generos.containsKey(id);
    }

    public synchronized GeneroDto crear(GeneroCrearDto dto) {
        String nombre = validarNombre(dto);
        comprobarNombreLibre(nombre, null);
        return aDto(insertar(nombre, limpiar(dto.getDescripcion())));
    }

    public synchronized GeneroDto modificar(Long id, GeneroCrearDto dto) {
        Genero genero = obtenerEntidad(id);
        String nombre = validarNombre(dto);
        comprobarNombreLibre(nombre, id);
        genero.setNombre(nombre);
        genero.setDescripcion(limpiar(dto.getDescripcion()));
        return aDto(genero);
    }

    /**
     * Elimina el género sin más comprobaciones.
     * NO llamar directamente desde un controlador: hay que pasar por CatalogoService.eliminarGenero, que comprueba que no tenga videojuegos.
     */
    public synchronized void eliminar(Long id) {
        obtenerEntidad(id);
        generos.remove(id);
    }

    public GeneroDto aDto(Genero genero) {
        return new GeneroDto(genero.getId(), genero.getNombre(), genero.getDescripcion());
    }

    // Métodos privados:

    private Genero insertar(String nombre, String descripcion) {
        Genero genero = new Genero(secuencia.incrementAndGet(), nombre, descripcion);
        generos.put(genero.getId(), genero);
        return genero;
    }

    private String validarNombre(GeneroCrearDto dto) {
        if (dto == null || dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new PeticionInvalidaException("El nombre de la categoría es obligatorio");
        }
        return dto.getNombre().trim();
    }

    private void comprobarNombreLibre(String nombre, Long idPropio) {
        boolean repetido = generos.values().stream()
                .anyMatch(g -> g.getNombre().equalsIgnoreCase(nombre) && !g.getId().equals(idPropio));
        if (repetido) {
            throw new ConflictoException("Ya existe una categoría llamada '" + nombre + "'");
        }
    }

    private String limpiar(String texto) {
        return texto == null ? null : texto.trim();
    }
}
