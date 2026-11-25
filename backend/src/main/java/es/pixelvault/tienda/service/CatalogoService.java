package es.pixelvault.tienda.service;

import es.pixelvault.tienda.dto.VideojuegoFiltroDto;
import es.pixelvault.tienda.dto.VideojuegoResumenDto;
import es.pixelvault.tienda.exception.ConflictoException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Aquí se coordina géneros y videojuegos. Resuelve la dependencia circular:
 *   GeneroService  <--  VideojuegoService  <--  CatalogoService
 * (cada flecha es "es utilizado por"; y no hay ciclos ni nada).
 */
@Service
public class CatalogoService {

    private final GeneroService generoService;
    private final VideojuegoService videojuegoService;

    public CatalogoService(GeneroService generoService, VideojuegoService videojuegoService) {
        this.generoService = generoService;
        this.videojuegoService = videojuegoService;
    }

    /* Regla: no se puede eliminar una categoría que tenga productos (activos o retirados). */
    public void eliminarGenero(Long id) {
        generoService.obtenerEntidad(id); // 404 si no existe
        long total = videojuegoService.contarPorGenero(id);
        if (total > 0) {
            throw new ConflictoException("No se puede eliminar la categoría porque tiene "
                    + total + " videojuego(s) asociado(s)");
        }
        generoService.eliminar(id);
    }

    /* Productos activos de una categoría (GET /api/categorias/{id}/productos). */
    public List<VideojuegoResumenDto> videojuegosDeGenero(Long id) {
        generoService.obtenerEntidad(id); // 404 si no existe
        VideojuegoFiltroDto filtro = new VideojuegoFiltroDto();
        filtro.setCategoriaId(id);
        return videojuegoService.listar(filtro);
    }
}
