package org.example.app;

import org.example.app.interfaces.RepositorioEnfermeras;
import org.example.domain.Enfermera;

public class AltaEnfermeraService {

    private final RepositorioEnfermeras repoEnfermeras;

    public AltaEnfermeraService(RepositorioEnfermeras repoEnfermeras) {
        this.repoEnfermeras = repoEnfermeras;
    }

    public Enfermera registrarEnfermera(
            String cuil,
            String nombre,
            String apellido
    ) {
        Enfermera enfermera = new Enfermera(nombre, apellido, cuil);
        repoEnfermeras.guardarEnfermera(enfermera);
        return enfermera;
    }
}


