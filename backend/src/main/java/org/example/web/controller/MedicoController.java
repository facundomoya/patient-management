package org.example.web.controller;

import org.example.app.AltaMedicoService;
import org.example.app.interfaces.RepositorioMedicos;
import org.example.domain.Exceptions.DomainException;
import org.example.domain.Medico;
import org.example.web.dto.AltaMedicoRequest;
import org.example.web.dto.MedicoDTO;
import org.example.web.mapper.MedicoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    private final AltaMedicoService altaMedicoService;
    private final RepositorioMedicos repositorioMedicos;

    public MedicoController(AltaMedicoService altaMedicoService,
                           RepositorioMedicos repositorioMedicos) {
        this.altaMedicoService = altaMedicoService;
        this.repositorioMedicos = repositorioMedicos;
    }

    // POST: alta de médico
    @PostMapping
    public ResponseEntity<?> crearMedico(@RequestBody AltaMedicoRequest request) {
        try {
            Medico medico = altaMedicoService.registrarMedico(
                    request.getCuil(),
                    request.getNombre(),
                    request.getApellido()
            );

            MedicoDTO dto = MedicoMapper.toDTO(medico);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (DomainException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno: " + e.getMessage());
        }
    }

    // GET: listar todos los médicos
    @GetMapping
    public ResponseEntity<List<MedicoDTO>> listarMedicos() {
        List<Medico> medicos = repositorioMedicos.listarTodos();
        List<MedicoDTO> dtos = medicos.stream()
                .map(MedicoMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}

