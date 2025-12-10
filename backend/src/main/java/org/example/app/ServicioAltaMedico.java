package org.example.app;

import org.example.app.interfaces.RepositorioMedicos;
import org.example.auth.app.ServicioAuth;
import org.example.domain.Medico;

public class ServicioAltaMedico {

    private final RepositorioMedicos repoMedicos;
    private ServicioAuth servicioAuth = null;

    public ServicioAltaMedico(RepositorioMedicos repoMedicos) {
        this.repoMedicos = repoMedicos;
    }

    public ServicioAltaMedico(
            RepositorioMedicos repoMedicos,
            ServicioAuth servicioAuth
    ) {
        this.repoMedicos = repoMedicos;
        this.servicioAuth = servicioAuth;
    }

    public Medico registrarMedico(
            String cuil,
            String nombre,
            String apellido
    ) {
        Medico medico = new Medico(nombre, apellido, cuil);
        repoMedicos.guardarMedico(medico);

        if (this.servicioAuth != null) {
            try {
                servicioAuth.inicializarUnUsuario(cuil);
            } catch (Exception e) {
                System.err.println("No se pudo inicializar usuario auth para enfermera " + cuil + ": " + e.getMessage());
            }
        }
        return medico;
    }
}


