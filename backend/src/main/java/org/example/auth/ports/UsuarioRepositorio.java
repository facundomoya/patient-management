package org.example.auth.ports;

import org.example.auth.domain.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepositorio {
    Usuario guardar(Usuario usuario);
    Optional<Usuario> buscarPorEmail(String email);
    boolean existePorEnfermera(String cuilEnfermera); // unicidad por actor
    boolean existePorMedico(String cuilMedico);
    List<Usuario> listarTodos(); // Para desarrollo/testing
}
