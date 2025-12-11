package org.example.infrastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.example.app.interfaces.RepositorioEnfermeras;
import org.example.domain.Enfermera;

public class RepositorioEnfermerasEnMemoria implements RepositorioEnfermeras {

    private final Map<String, Enfermera> enfermeras = new HashMap<>();

    public RepositorioEnfermerasEnMemoria() {
        inicializarDatosMock();
    }

    private void inicializarDatosMock() {
        Enfermera e1 = new Enfermera(
                "IGNACIO",
                "SANCHEZ",
                "20-43336577-2"
        );
        guardarEnfermera(e1);
    }

    @Override
    public void guardarEnfermera(Enfermera enfermera) {
        enfermeras.put(enfermera.getCuil(), enfermera);
    }

    @Override
    public Optional<Enfermera> buscarEnfermeraPorCuil(String cuil) {
        return Optional.ofNullable(enfermeras.get(cuil));
    }

    @Override
    public List<Enfermera> listarTodas() {
        return new ArrayList<>(enfermeras.values());
    }
}

