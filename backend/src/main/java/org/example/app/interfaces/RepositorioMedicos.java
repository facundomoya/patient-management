package org.example.app.interfaces;

import org.example.domain.Medico;

import java.util.List;
import java.util.Optional;

public interface RepositorioMedicos {

    void guardarMedico(Medico medico);

    Optional<Medico> buscarMedicoPorCuil(String cuil);

    List<Medico> listarTodos();
}

