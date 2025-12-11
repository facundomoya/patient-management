package org.example.web.controller;

import org.example.auth.app.ServicioAuth;
import org.example.auth.domain.Usuario;
import org.example.auth.ports.UsuarioRepositorio;
import org.example.domain.Exceptions.DomainException;
import org.example.web.dto.LoginRequest;
import org.example.web.dto.UsuarioDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final ServicioAuth servicioAuth;
    private final UsuarioRepositorio usuarioRepositorio;

    public AuthController(ServicioAuth servicioAuth, UsuarioRepositorio usuarioRepositorio) {
        this.servicioAuth = servicioAuth;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Usuario usuario = servicioAuth.login(request.getEmail(), request.getPassword());
            
            String nombre, apellido;
            if (usuario.esEnfermera()) {
                nombre = usuario.getEnfermera().getNombre();
                apellido = usuario.getEnfermera().getApellido();
            } else {
                nombre = usuario.getMedico().getNombre();
                apellido = usuario.getMedico().getApellido();
            }

            UsuarioDTO dto = new UsuarioDTO(
                usuario.getEmail(),
                usuario.getRol(),
                usuario.getCuilActor(),
                nombre,
                apellido
            );

            return ResponseEntity.ok(dto);
        } catch (DomainException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno: " + e.getMessage());
        }
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        List<Usuario> usuarios = usuarioRepositorio.listarTodos();
        
        List<UsuarioDTO> dtos = usuarios.stream()
                .map(usuario -> {
                    String nombre, apellido;
                    if (usuario.esEnfermera()) {
                        nombre = usuario.getEnfermera().getNombre();
                        apellido = usuario.getEnfermera().getApellido();
                    } else {
                        nombre = usuario.getMedico().getNombre();
                        apellido = usuario.getMedico().getApellido();
                    }

                    String hash = usuario.getHash();
                    String hashPreview = hash != null && hash.length() > 20 
                            ? hash.substring(0, 20) + "..." 
                            : hash;

                    return new UsuarioDTO(
                            usuario.getEmail(),
                            usuario.getRol(),
                            usuario.getCuilActor(),
                            nombre,
                            apellido,
                            hashPreview
                    );
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}

