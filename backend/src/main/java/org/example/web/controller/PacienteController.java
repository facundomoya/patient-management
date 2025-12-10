package org.example.web.controller;

import org.example.app.ServicioAltaPaciente;
import org.example.app.interfaces.RepositorioPacientes;
import org.example.domain.Exceptions.DomainException;
import org.example.domain.Paciente;
import org.example.web.dto.AltaPacienteRequest;
import org.example.web.dto.DomicilioDTO;
import org.example.web.dto.PacienteDTO;
import org.example.web.mapper.PacienteMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final ServicioAltaPaciente altaPacienteService;
    private final RepositorioPacientes repositorioPacientes;

    public PacienteController(ServicioAltaPaciente altaPacienteService,
                              RepositorioPacientes repositorioPacientes) {
        this.altaPacienteService = altaPacienteService;
        this.repositorioPacientes = repositorioPacientes;
    }

    // POST: alta de paciente
    @PostMapping
    public ResponseEntity<?> crearPaciente(@RequestBody AltaPacienteRequest request) {
        try {
            DomicilioDTO dom = request.getDomicilio();
            Paciente paciente = altaPacienteService.registrarPaciente(
                    request.getCuil(),
                    request.getNombre(),
                    request.getApellido(),
                    dom.getCalle(),
                    dom.getNumero(),
                    dom.getLocalidad(),
                    request.getObraSocialCodigo(),
                    request.getNumeroAfiliado()
            );

            PacienteDTO dto = PacienteMapper.toDTO(paciente);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (DomainException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno: " + e.getMessage());
        }
    }

    // GET: listar todos los pacientes
    @GetMapping
    public ResponseEntity<List<PacienteDTO>> listarPacientes() {
        List<Paciente> pacientes = altaPacienteService.listarPacientes();
        List<PacienteDTO> dtos = pacientes.stream()
                .map(PacienteMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}
