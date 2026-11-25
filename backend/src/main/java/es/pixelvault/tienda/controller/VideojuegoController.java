package es.pixelvault.tienda.controller;

import es.pixelvault.tienda.dto.VideojuegoDetalleDto;
import es.pixelvault.tienda.dto.VideojuegoFiltroDto;
import es.pixelvault.tienda.dto.VideojuegoResumenDto;
import es.pixelvault.tienda.model.Direccion;
import es.pixelvault.tienda.model.OrdenProducto;
import es.pixelvault.tienda.model.Plataforma;
import es.pixelvault.tienda.service.VideojuegoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/*
 * Catálogo público (autenticado). Spring da prioridad a las rutas literales
 * (/destacados, /sku/{sku}) sobre /{id}, y /{id} es Long, así que "destacados" nunca se confunde con un id.
 */
@RestController
@RequestMapping("/api/productos")
public class VideojuegoController {

    private final VideojuegoService videojuegoService;

    public VideojuegoController(VideojuegoService videojuegoService) {
        this.videojuegoService = videojuegoService;
    }

    @GetMapping
    public ResponseEntity<List<VideojuegoResumenDto>> listar(
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) BigDecimal precioMin,
            @RequestParam(required = false) BigDecimal precioMax,
            @RequestParam(required = false) Boolean soloDisponibles,
            @RequestParam(required = false) OrdenProducto orden,
            @RequestParam(required = false) Direccion direccion,
            @RequestParam(required = false) Plataforma plataforma) {
        VideojuegoFiltroDto filtro = new VideojuegoFiltroDto();
        filtro.setCategoriaId(categoriaId);
        filtro.setQ(q);
        filtro.setPrecioMin(precioMin);
        filtro.setPrecioMax(precioMax);
        filtro.setSoloDisponibles(soloDisponibles);
        filtro.setOrden(orden);
        filtro.setDireccion(direccion);
        filtro.setPlataforma(plataforma);
        return ResponseEntity.ok(videojuegoService.listar(filtro));
    }

    @GetMapping("/destacados")
    public ResponseEntity<List<VideojuegoResumenDto>> destacados() {
        return ResponseEntity.ok(videojuegoService.destacados());
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<VideojuegoDetalleDto> porSku(@PathVariable String sku) {
        return ResponseEntity.ok(videojuegoService.porSku(sku));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideojuegoDetalleDto> detalle(@PathVariable Long id) {
        return ResponseEntity.ok(videojuegoService.detalle(id));
    }
}
