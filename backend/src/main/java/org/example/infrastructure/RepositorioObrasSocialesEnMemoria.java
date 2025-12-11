package org.example.infrastructure;

import org.example.app.interfaces.RepositorioObrasSociales;
import org.example.domain.ObraSocial;

import java.util.*;

public class RepositorioObrasSocialesEnMemoria implements RepositorioObrasSociales {
    private final Map<String, ObraSocial> obrasSociales = new HashMap<>();

    public RepositorioObrasSocialesEnMemoria() {
        inicializarDatosMock();
    }

    private void inicializarDatosMock() {
        guardar(new ObraSocial("OSDE", "OSDE"));
        guardar(new ObraSocial("OSECAC", "OSECAC"));
        guardar(new ObraSocial("SWISS", "Swiss Medical"));
        guardar(new ObraSocial("GALENO", "Galeno"));
        guardar(new ObraSocial("OMINT", "Omint"));
    }

    public void guardar(ObraSocial os) {
        obrasSociales.put(os.getCodigo(), os);
    }

    @Override
    public Optional<ObraSocial> buscarPorCodigo(String codigo) {
        return Optional.ofNullable(obrasSociales.get(codigo));
    }

    public List<ObraSocial> listarTodas() {
        return new ArrayList<>(obrasSociales.values());
    }
}

