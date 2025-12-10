package org.example.web.controller;

import org.example.app.ServicioRegistroAtencion;
import org.example.app.interfaces.RepositorioIngresos;
import org.example.auth.domain.Usuario;
import org.example.auth.ports.UsuarioRepositorio;
import org.example.domain.Exceptions.DomainException;
import org.example.domain.Ingreso;
import org.example.domain.Medico;
import org.example.web.dto.RegistrarAtencionRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/atencion")
public class AtencionController {

    private final ServicioRegistroAtencion moduloRegistroAtencion;
    private final UsuarioRepositorio usuarioRepositorio;
    private final RepositorioIngresos repositorioIngresos;

    public AtencionController(ServicioRegistroAtencion moduloRegistroAtencion,
                               UsuarioRepositorio usuarioRepositorio,
                               RepositorioIngresos repositorioIngresos) {
        this.moduloRegistroAtencion = moduloRegistroAtencion;
        this.usuarioRepositorio = usuarioRepositorio;
        this.repositorioIngresos = repositorioIngresos;
    }

    @PostMapping
    public ResponseEntity<?> registrarAtencion(@RequestBody RegistrarAtencionRequest request,
                                                @RequestHeader(value = "X-User-Email", required = false) String userEmail) {
        try {
            // Validar que el usuario esté autenticado
            if (userEmail == null || userEmail.isBlank()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Usuario no autenticado");
            }

            // Obtener el usuario de la sesión
            Usuario usuario = usuarioRepositorio.buscarPorEmail(userEmail)
                    .orElseThrow(() -> new DomainException("Usuario no encontrado"));

            // Validar que sea médico
            if (!usuario.esMedico()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Solo los médicos pueden registrar atenciones");
            }

            // Obtener el médico del usuario
            Medico medico = usuario.getMedico();

            // Buscar el ingreso por CUIL del paciente en los ingresos en proceso
            Ingreso ingreso = repositorioIngresos.obtenerEnProceso().stream()
                    .filter(i -> i.getCuilPaciente().equals(request.getCuilPaciente()))
                    .findFirst()
                    .orElseThrow(() -> new DomainException("No se encontró un ingreso en proceso para el paciente"));

            // Registrar la atención
            moduloRegistroAtencion.registrarAtencion(
                    ingreso,
                    medico,
                    request.getInforme(),
                    LocalDateTime.now()
            );

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (DomainException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno: " + e.getMessage());
        }
    }
}

