package org.example.infrastructure;

import org.example.app.interfaces.RepositorioMedicos;
import org.example.auth.ports.MedicoRepositorio;
import org.example.domain.Medico;

import java.util.Optional;

public class MedicoRepositorioAdapter implements MedicoRepositorio {
    private final RepositorioMedicos repoMedicos;

    public MedicoRepositorioAdapter(RepositorioMedicos repoMedicos) {
        this.repoMedicos = repoMedicos;
    }

    @Override
    public Optional<Medico> buscarPorCuil(String cuil) {
        return repoMedicos.buscarMedicoPorCuil(cuil);
    }
}

