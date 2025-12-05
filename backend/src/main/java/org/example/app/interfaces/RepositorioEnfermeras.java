package org.example.app.interfaces;

import org.example.domain.Enfermera;

import java.util.List;
import java.util.Optional;

public interface RepositorioEnfermeras {

    void guardarEnfermera(Enfermera enfermera);

    Optional<Enfermera> buscarEnfermeraPorCuil(String cuil);

    List<Enfermera> listarTodas();
}

