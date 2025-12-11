package org.example.infrastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.example.auth.domain.Usuario;
import org.example.auth.ports.UsuarioRepositorio;

public class RepositorioUsuariosEnMemoria implements UsuarioRepositorio {
    private final Map<String, Usuario> porEmail = new HashMap<>();
    private final Set<String> porEnfermera = new HashSet<>(); 
    private final Set<String> porMedico = new HashSet<>();  

    @Override
    public Usuario guardar(Usuario u) {
        porEmail.put(u.getEmail(), u);
        if (u.esEnfermera()) porEnfermera.add(u.getCuilActor());
        else                 porMedico.add(u.getCuilActor());
        return u;
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return Optional.ofNullable(porEmail.get(email));
    }

    @Override
    public boolean existePorEnfermera(String cuil) {
        return porEnfermera.contains(cuil);
    }

    @Override
    public boolean existePorMedico(String cuil) {
        return porMedico.contains(cuil);
    }

    @Override
    public List<Usuario> listarTodos() {
        return new ArrayList<>(porEmail.values());
    }
}

