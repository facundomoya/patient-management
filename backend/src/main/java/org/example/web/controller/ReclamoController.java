package org.example.web.controller;

import org.example.app.ModuloReclamo;
import org.example.auth.domain.Usuario;
import org.example.auth.ports.UsuarioRepositorio;
import org.example.app.interfaces.RepositorioIngresos;
import org.example.domain.Exceptions.DomainException;
import org.example.domain.Ingreso;
import org.example.web.dto.IngresoDTO;
import org.example.web.mapper.IngresoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reclamo")
public class ReclamoController {

    private final ModuloReclamo moduloReclamo;
    private final UsuarioRepositorio usuarioRepositorio;
    private final RepositorioIngresos repositorioIngresos;

    public ReclamoController(ModuloReclamo moduloReclamo, 
                             UsuarioRepositorio usuarioRepositorio,
                             RepositorioIngresos repositorioIngresos) {
        this.moduloReclamo = moduloReclamo;
        this.usuarioRepositorio = usuarioRepositorio;
        this.repositorioIngresos = repositorioIngresos;
    }

    @PostMapping
    public ResponseEntity<?> reclamarSiguientePaciente(
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
                        .body("Solo los médicos pueden reclamar pacientes");
            }

            // Reclamar siguiente paciente (cambia estado a EN_PROCESO)
            Ingreso ingreso = moduloReclamo.reclamarSiguientePaciente();
            
            // Convertir a DTO
            IngresoDTO dto = IngresoMapper.toDTO(ingreso);
            
            return ResponseEntity.ok(dto);
        } catch (DomainException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno: " + e.getMessage());
        }
    }

    @GetMapping("/actual")
    public ResponseEntity<?> obtenerPacienteEnAtencion(
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
                        .body("Solo los médicos pueden ver pacientes en atención");
            }

            // Obtener todos los ingresos en proceso
            List<Ingreso> ingresosEnProceso = repositorioIngresos.obtenerEnProceso();
            
            // Buscar si hay algún ingreso en proceso (por ahora, devolvemos el primero si existe)
            // En el futuro podrías asociar el ingreso con el médico específico
            Optional<Ingreso> ingresoEnProceso = ingresosEnProceso.stream()
                    .findFirst();
            
            if (ingresoEnProceso.isPresent()) {
                IngresoDTO dto = IngresoMapper.toDTO(ingresoEnProceso.get());
                return ResponseEntity.ok(dto);
            } else {
                return ResponseEntity.ok(null);
            }
        } catch (DomainException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno: " + e.getMessage());
        }
    }
}
