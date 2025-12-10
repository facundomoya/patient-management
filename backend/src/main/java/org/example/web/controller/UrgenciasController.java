// src/main/java/org/example/web/controller/UrgenciasController.java
package org.example.web.controller;

import org.example.app.ServicioUrgencias;
import org.example.auth.domain.Usuario;
import org.example.auth.ports.UsuarioRepositorio;
import org.example.domain.Enfermera;
import org.example.domain.Exceptions.DomainException;
import org.example.domain.Ingreso;
import org.example.domain.NivelEmergencia;
import org.example.web.dto.IngresoDTO;
import org.example.web.dto.AltaUrgenciaRequest;
import org.example.web.mapper.IngresoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/urgencias")
public class UrgenciasController {

    private final ServicioUrgencias servicioUrgencias;
    private final UsuarioRepositorio usuarioRepositorio;

    public UrgenciasController(ServicioUrgencias servicioUrgencias,
                               UsuarioRepositorio usuarioRepositorio) {
        this.servicioUrgencias = servicioUrgencias;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @PostMapping
    public ResponseEntity<?> registrarUrgencia(
            @RequestBody AltaUrgenciaRequest req,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {
        try {
            // Validar que el usuario esté autenticado
            if (userEmail == null || userEmail.isBlank()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Usuario no autenticado");
            }

            // Obtener el usuario de la sesión
            Usuario usuario = usuarioRepositorio.buscarPorEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Validar que sea enfermera
            if (!usuario.esEnfermera()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Solo las enfermeras pueden registrar urgencias");
            }

            // Obtener la enfermera del usuario
            Enfermera enfermera = usuario.getEnfermera();

            // Buscar el enum de nivel igual que en los steps de Cucumber
            NivelEmergencia nivel = Arrays.stream(NivelEmergencia.values())
                    .filter(ne -> ne.tieneNombre(req.getNivelEmergencia()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Nivel de emergencia desconocido: " + req.getNivelEmergencia()));

            servicioUrgencias.registrarUrgencia(
                    req.getCuilPaciente(),
                    enfermera,
                    req.getInforme(),
                    nivel,
                    req.getTemperatura(),
                    req.getFrecuenciaCardiaca(),
                    req.getFrecuenciaRespiratoria(),
                    req.getTensionSistolica(),
                    req.getTensionDiastolica()
            );

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (DomainException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Podés refinar esto según DomainException, etc.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/lista-espera")
    public ResponseEntity<List<IngresoDTO>> obtenerListaEspera() {
        List<Ingreso> ingresos = servicioUrgencias.obtenerIngresosPendientes();
        List<IngresoDTO> dtos = ingresos.stream()
                .map(IngresoMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/ingresos")
    public ResponseEntity<List<IngresoDTO>> obtenerTodosLosIngresos() {
        List<IngresoDTO> dtos = servicioUrgencias.obtenerTodosLosIngresos()
                .stream()
                .map(IngresoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
