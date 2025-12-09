package org.example.web.controller;

import org.example.app.interfaces.RepositorioAtenciones;
import org.example.domain.Atencion;
import org.example.web.dto.AtencionDTO;
import org.example.web.mapper.AtencionMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<AtencionDTO>> listarAtenciones() {
        // Obtener solo atenciones donde el ingreso esta FINALIZADO
        List<Atencion> atenciones = repositorioAtenciones.obtenerFinalizadas();
        
        // Ordenar por fecha de atención (más reciente primero)
        List<AtencionDTO> dtos = atenciones.stream()
                .sorted(Comparator.comparing(Atencion::getFechaAtencion).reversed())
                .map(AtencionMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(dtos);
    }
}