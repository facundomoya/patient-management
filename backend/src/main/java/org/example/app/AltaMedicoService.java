package org.example.app;

import org.example.app.interfaces.RepositorioMedicos;
import org.example.domain.Medico;

public class AltaMedicoService {

    private final RepositorioMedicos repoMedicos;

    public AltaMedicoService(RepositorioMedicos repoMedicos) {
        this.repoMedicos = repoMedicos;
    }

    public Medico registrarMedico(
            String cuil,
            String nombre,
            String apellido
    ) {
        Medico medico = new Medico(nombre, apellido, cuil);
        repoMedicos.guardarMedico(medico);
        return medico;
    }
}


