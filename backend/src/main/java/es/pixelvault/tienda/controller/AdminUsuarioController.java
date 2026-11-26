package es.pixelvault.tienda.controller;

import es.pixelvault.tienda.dto.CambioRolDto;
import es.pixelvault.tienda.dto.UsuarioDto;
import es.pixelvault.tienda.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/usuarios")
public class AdminUsuarioController {

    private final UsuarioService usuarioService;

    public AdminUsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDto>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @PutMapping("/{id}/rol")
    public ResponseEntity<UsuarioDto> cambiarRol(@PathVariable Long id, @RequestBody CambioRolDto dto) {
        return ResponseEntity.ok(usuarioService.cambiarRol(id, dto.getRol()));
    }
}