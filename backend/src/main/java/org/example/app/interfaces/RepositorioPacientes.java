package org.example.app.interfaces;

import org.example.domain.Paciente;

import java.util.List;
import java.util.Optional;

public interface RepositorioPacientes {

    void guardarPaciente(Paciente paciente);

    Optional<Paciente> buscarPacientePorCuil(String cuil);

    // Nuevo metodo para listar todos los pacientes
    List<Paciente> listarTodos();
}
