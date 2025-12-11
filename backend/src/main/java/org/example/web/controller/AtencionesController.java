package org.example.web.controller;

import org.example.app.interfaces.RepositorioAtenciones;
import org.example.domain.Atencion;
import org.example.web.dto.AtencionDTO;
import org.example.web.mapper.AtencionMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/atenciones")
public class AtencionesController {

    private final RepositorioAtenciones repositorioAtenciones;

    public AtencionesController(RepositorioAtenciones repositorioAtenciones) {
        this.repositorioAtenciones = repositorioAtenciones;
    }

    @GetMapping
    public ResponseEntity<List<AtencionDTO>> listarAtencionesFinalizadas() {
        List<Atencion> atenciones = repositorioAtenciones.obtenerFinalizadas();

        List<AtencionDTO> dtos = atenciones.stream()
                .sorted(Comparator.comparing(Atencion::getFechaAtencion).reversed())
                .map(AtencionMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/todas")
    public ResponseEntity<List<AtencionDTO>> listarTodasLasAtenciones() {
        List<Atencion> atenciones = repositorioAtenciones.obtenerAtenciones();

        List<AtencionDTO> dtos = atenciones.stream()
                .sorted(Comparator.comparing(Atencion::getFechaAtencion).reversed())
                .map(AtencionMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}
