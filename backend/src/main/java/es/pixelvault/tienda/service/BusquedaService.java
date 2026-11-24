package es.pixelvault.tienda.service;

import es.pixelvault.tienda.dto.VideojuegoFiltroDto;
import es.pixelvault.tienda.exception.PeticionInvalidaException;
import es.pixelvault.tienda.model.Videojuego;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.Collection;
import java.util.List;

/**
 * Aplica los filtros del catálogo con streams. Esto no guarda datos ni ordena:
 * recibe la colección base y devuelve los elementos que cumplen todos los filtros
 * indicados (los que vienen a null se ignoran). La ordenación la hace VideojuegoService.
 */
@Service
public class BusquedaService {

    public List<Videojuego> filtrar(Collection<Videojuego> base, VideojuegoFiltroDto filtro) {
        if (filtro == null) {
            return List.copyOf(base);
        }
        validarRangoPrecio(filtro.getPrecioMin(), filtro.getPrecioMax());

        String texto = filtro.getQ() == null || filtro.getQ().isBlank() ? null : normalizar(filtro.getQ());
        boolean soloDisponibles = Boolean.TRUE.equals(filtro.getSoloDisponibles());

        return base.stream()
                .filter(v -> filtro.getCategoriaId() == null
                        || v.getGenero().getId().equals(filtro.getCategoriaId()))
                .filter(v -> texto == null
                        || normalizar(v.getNombre()).contains(texto)
                        || normalizar(v.getDescripcion()).contains(texto))
                .filter(v -> filtro.getPrecioMin() == null || v.getPrecio().compareTo(filtro.getPrecioMin()) >= 0)
                .filter(v -> filtro.getPrecioMax() == null || v.getPrecio().compareTo(filtro.getPrecioMax()) <= 0)
                .filter(v -> !soloDisponibles || v.getStock() > 0)
                .filter(v -> filtro.getPlataforma() == null || v.getPlataforma() == filtro.getPlataforma())
                .toList();
    }

    /** Minúsculas y sin tildes: "Acción" y "accion" se consideran iguales. */
    public static String normalizar(String texto) {
        if (texto == null) {
            return "";
        }
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();
    }

    private void validarRangoPrecio(BigDecimal min, BigDecimal max) {
        if (min != null && min.signum() < 0) {
            throw new PeticionInvalidaException("El precio mínimo no puede ser negativo");
        }
        if (min != null && max != null && min.compareTo(max) > 0) {
            throw new PeticionInvalidaException("El precio mínimo no puede ser mayor que el precio máximo");
        }
    }
}
