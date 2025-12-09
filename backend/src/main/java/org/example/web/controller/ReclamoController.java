package org.example.web.controller;

import org.example.app.ModuloReclamo;
import org.example.auth.domain.Usuario;
import org.example.auth.ports.UsuarioRepositorio;
import org.example.app.interfaces.RepositorioIngresos;
import org.example.app.interfaces.RepositorioAtenciones;
import org.example.domain.Atencion;
import org.example.domain.Exceptions.DomainException;
import org.example.domain.Ingreso;
import org.example.domain.Medico;
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
    private final RepositorioAtenciones repositorioAtenciones;

    public ReclamoController(ModuloReclamo moduloReclamo, 
                             UsuarioRepositorio usuarioRepositorio,
                             RepositorioIngresos repositorioIngresos,
                             RepositorioAtenciones repositorioAtenciones) {
        this.moduloReclamo = moduloReclamo;
        this.usuarioRepositorio = usuarioRepositorio;
        this.repositorioIngresos = repositorioIngresos;
        this.repositorioAtenciones = repositorioAtenciones;
    }

    @PostMapping
    public ResponseEntity<?> reclamarSiguientePaciente(
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {
        try {
            // Validar que el usuario este autenticado
            if (userEmail == null || userEmail.isBlank()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Usuario no autenticado");
            }

            // Obtener el usuario de la sesion
            Usuario usuario = usuarioRepositorio.buscarPorEmail(userEmail)
                    .orElseThrow(() -> new DomainException("Usuario no encontrado"));

            // Validar que sea medico
            if (!usuario.esMedico()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Solo los medicos pueden reclamar pacientes");
            }

            // Reclamar siguiente paciente (cambia estado a EN_PROCESO)
            Medico medico = usuario.getMedico();
            if (medico == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El usuario no tiene asociada una entidad de medico");
            }
            Ingreso ingreso = moduloReclamo.reclamarSiguientePaciente(medico);
            
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
            // Validar que el usuario este autenticado
            if (userEmail == null || userEmail.isBlank()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Usuario no autenticado");
            }

            // Obtener el usuario de la sesion
            Usuario usuario = usuarioRepositorio.buscarPorEmail(userEmail)
                    .orElseThrow(() -> new DomainException("Usuario no encontrado"));

            // Validar que sea medico
            if (!usuario.esMedico()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Solo los medicos pueden ver pacientes en atencion");
            }

            // Obtener el medico del usuario
            Medico medico = usuario.getMedico();
            if (medico == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El usuario no tiene asociada una entidad de medico");
            }

            // Obtener la Atencion activa (EN_PROCESO) del medico
            Optional<Atencion> atencionOpt = repositorioAtenciones.obtenerAtencionActivaPorMedico(medico);
            
            if (atencionOpt.isPresent()) {
                Atencion atencion = atencionOpt.get();
                Ingreso ingreso = atencion.getIngreso();
                IngresoDTO dto = IngresoMapper.toDTO(ingreso);
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