package org.example.web.controller;

import org.example.app.AltaEnfermeraService;
import org.example.app.interfaces.RepositorioEnfermeras;
import org.example.domain.Exceptions.DomainException;
import org.example.domain.Enfermera;
import org.example.web.dto.AltaEnfermeraRequest;
import org.example.web.dto.EnfermeraDTO;
import org.example.web.mapper.EnfermeraMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/enfermeras")
public class EnfermeraController {

    private final AltaEnfermeraService altaEnfermeraService;
    private final RepositorioEnfermeras repositorioEnfermeras;

    public EnfermeraController(AltaEnfermeraService altaEnfermeraService,
                              RepositorioEnfermeras repositorioEnfermeras) {
        this.altaEnfermeraService = altaEnfermeraService;
        this.repositorioEnfermeras = repositorioEnfermeras;
    }

    // POST: alta de enfermera
    @PostMapping
    public ResponseEntity<?> crearEnfermera(@RequestBody AltaEnfermeraRequest request) {
        try {
            Enfermera enfermera = altaEnfermeraService.registrarEnfermera(
                    request.getCuil(),
                    request.getNombre(),
                    request.getApellido()
            );

            EnfermeraDTO dto = EnfermeraMapper.toDTO(enfermera);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (DomainException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno: " + e.getMessage());
        }
    }

    // GET: listar todas las enfermeras
    @GetMapping
    public ResponseEntity<List<EnfermeraDTO>> listarEnfermeras() {
        List<Enfermera> enfermeras = repositorioEnfermeras.listarTodas();
        List<EnfermeraDTO> dtos = enfermeras.stream()
                .map(EnfermeraMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}

