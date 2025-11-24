// src/main/java/org/example/web/controller/UrgenciasController.java
package org.example.web.controller;

import org.example.app.ServicioUrgencias;
import org.example.domain.Enfermera;
import org.example.domain.Ingreso;
import org.example.domain.NivelEmergencia;
import org.example.web.dto.IngresoDTO;
import org.example.web.dto.RegistrarUrgenciaRequest;
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

    public UrgenciasController(ServicioUrgencias servicioUrgencias) {
        this.servicioUrgencias = servicioUrgencias;
    }

    @PostMapping
    public ResponseEntity<?> registrarUrgencia(@RequestBody RegistrarUrgenciaRequest req) {
        try {
            // Enfermera de la atención
            Enfermera enfermera = new Enfermera(
                    req.getEnfermeraNombre(),
                    req.getEnfermeraApellido(),
                    req.getEnfermeraCuil()
            );

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
}
