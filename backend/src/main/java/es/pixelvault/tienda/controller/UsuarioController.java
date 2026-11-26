package es.pixelvault.tienda.controller;

import es.pixelvault.tienda.dto.UsuarioActualizarDto;
import es.pixelvault.tienda.dto.UsuarioDto;
import es.pixelvault.tienda.service.UsuarioActualService;
import es.pixelvault.tienda.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioActualService usuarioActualService;

    public UsuarioController(UsuarioService usuarioService, UsuarioActualService usuarioActualService) {
        this.usuarioService = usuarioService;
        this.usuarioActualService = usuarioActualService;
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioDto> me() {
        return ResponseEntity.ok(usuarioService.aDto(usuarioActualService.obtenerUsuario()));
    }

    @PutMapping("/me")
    public ResponseEntity<UsuarioDto> actualizar(@RequestBody UsuarioActualizarDto dto) {
        return ResponseEntity.ok(usuarioService.actualizarPerfil(usuarioActualService.obtenerUsuario(), dto));
    }
}