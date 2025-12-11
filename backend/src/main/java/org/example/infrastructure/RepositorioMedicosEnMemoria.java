package org.example.infrastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.example.app.interfaces.RepositorioMedicos;
import org.example.domain.Medico;

public class RepositorioMedicosEnMemoria implements RepositorioMedicos {

    private final Map<String, Medico> medicos = new HashMap<>();

    public RepositorioMedicosEnMemoria() {
        inicializarDatosMock();
    }

    private void inicializarDatosMock() {
        Medico m1 = new Medico(
                "EDGARDO",
                "ALONSO",
                "20-43336577-2"
        );

        guardarMedico(m1);
    }

    @Override
    public void guardarMedico(Medico medico) {
        medicos.put(medico.getCuil(), medico);
    }

    @Override
    public Optional<Medico> buscarMedicoPorCuil(String cuil) {
        return Optional.ofNullable(medicos.get(cuil));
    }

    @Override
    public List<Medico> listarTodos() {
        return new ArrayList<>(medicos.values());
    }
}

