package es.pixelvault.tienda.controller;

import es.pixelvault.tienda.dto.StockDto;
import es.pixelvault.tienda.dto.VideojuegoCrearDto;
import es.pixelvault.tienda.dto.VideojuegoDetalleDto;
import es.pixelvault.tienda.service.VideojuegoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/productos")
public class AdminVideojuegoController {

    private final VideojuegoService videojuegoService;

    public AdminVideojuegoController(VideojuegoService videojuegoService) {
        this.videojuegoService = videojuegoService;
    }

    @GetMapping
    public ResponseEntity<List<VideojuegoDetalleDto>> listar() {
        return ResponseEntity.ok(videojuegoService.listarAdmin());
    }

    @GetMapping("/stock-bajo")
    public ResponseEntity<List<VideojuegoDetalleDto>> stockBajo(@RequestParam(defaultValue = "5") int umbral) {
        return ResponseEntity.ok(videojuegoService.stockBajo(umbral));
    }

    @PostMapping
    public ResponseEntity<VideojuegoDetalleDto> crear(@RequestBody VideojuegoCrearDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(videojuegoService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VideojuegoDetalleDto> modificar(@PathVariable Long id, @RequestBody VideojuegoCrearDto dto) {
        return ResponseEntity.ok(videojuegoService.modificar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> retirar(@PathVariable Long id) {
        videojuegoService.retirar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<VideojuegoDetalleDto> ajustarStock(@PathVariable Long id, @RequestBody StockDto dto) {
        return ResponseEntity.ok(videojuegoService.ajustarStock(id, dto));
    }

    @PutMapping("/{id}/reactivar")
    public ResponseEntity<VideojuegoDetalleDto> reactivar(@PathVariable Long id) {
        return ResponseEntity.ok(videojuegoService.reactivar(id));
    }
}
