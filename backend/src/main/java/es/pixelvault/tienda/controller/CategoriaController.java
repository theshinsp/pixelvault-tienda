package es.pixelvault.tienda.controller;

import es.pixelvault.tienda.dto.GeneroDto;
import es.pixelvault.tienda.dto.VideojuegoResumenDto;
import es.pixelvault.tienda.service.CatalogoService;
import es.pixelvault.tienda.service.GeneroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/* Consulta de categorías (solo para cualquier usuario autenticado). */
@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final GeneroService generoService;
    private final CatalogoService catalogoService;

    public CategoriaController(GeneroService generoService, CatalogoService catalogoService) {
        this.generoService = generoService;
        this.catalogoService = catalogoService;
    }

    @GetMapping
    public ResponseEntity<List<GeneroDto>> listar() {
        return ResponseEntity.ok(generoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneroDto> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(generoService.obtener(id));
    }

    @GetMapping("/{id}/productos")
    public ResponseEntity<List<VideojuegoResumenDto>> productos(@PathVariable Long id) {
        return ResponseEntity.ok(catalogoService.videojuegosDeGenero(id));
    }
}
