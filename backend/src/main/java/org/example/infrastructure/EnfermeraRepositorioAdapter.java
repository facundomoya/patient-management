package org.example.infrastructure;

import org.example.app.interfaces.RepositorioEnfermeras;
import org.example.auth.ports.EnfermeraRepositorio;
import org.example.domain.Enfermera;

import java.util.Optional;

public class EnfermeraRepositorioAdapter implements EnfermeraRepositorio {
    private final RepositorioEnfermeras repoEnfermeras;

    public EnfermeraRepositorioAdapter(RepositorioEnfermeras repoEnfermeras) {
        this.repoEnfermeras = repoEnfermeras;
    }

    @Override
    public Optional<Enfermera> buscarPorCuil(String cuil) {
        return repoEnfermeras.buscarEnfermeraPorCuil(cuil);
    }
}

