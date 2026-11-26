package es.pixelvault.tienda.config;

import es.pixelvault.tienda.dto.RegistroDto;
import es.pixelvault.tienda.service.AuthService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Crea al arrancar los dos usuarios de prueba documentados en el README.
 * Son credenciales de demostración (los datos viven en memoria), no secretos reales.
 * admin@pixelvault.com recibe el rol ADMIN porque está en tienda.admins.
 */
@Component
public class UsuariosPruebaInicializador implements ApplicationRunner {

    private final AuthService authService;

    public UsuariosPruebaInicializador(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void run(ApplicationArguments args) {
        registrar("cliente@pixelvault.com", "Cliente de Prueba", "Cliente123!");
        registrar("admin@pixelvault.com", "Administrador de Prueba", "Admin12345!");
    }

    private void registrar(String email, String nombre, String password) {
        RegistroDto dto = new RegistroDto();
        dto.setEmail(email);
        dto.setNombre(nombre);
        dto.setPassword(password);
        authService.registrar(dto);
    }
}
