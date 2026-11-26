package es.pixelvault.tienda.controller;

import es.pixelvault.tienda.dto.GeneroCrearDto;
import es.pixelvault.tienda.dto.GeneroDto;
import es.pixelvault.tienda.service.CatalogoService;
import es.pixelvault.tienda.service.GeneroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/* Administración de categorías. Protegido por SecurityConfig: /api/admin/** solo ADMIN. */
@RestController
@RequestMapping("/api/admin/categorias")
public class AdminCategoriaController {

    private final GeneroService generoService;
    private final CatalogoService catalogoService;

    public AdminCategoriaController(GeneroService generoService, CatalogoService catalogoService) {
        this.generoService = generoService;
        this.catalogoService = catalogoService;
    }

    @GetMapping
    public ResponseEntity<List<GeneroDto>> listar() {
        return ResponseEntity.ok(generoService.listar());
    }

    @PostMapping
    public ResponseEntity<GeneroDto> crear(@RequestBody GeneroCrearDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(generoService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneroDto> modificar(@PathVariable Long id, @RequestBody GeneroCrearDto dto) {
        return ResponseEntity.ok(generoService.modificar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        catalogoService.eliminarGenero(id);
        return ResponseEntity.noContent().build();
    }
}
