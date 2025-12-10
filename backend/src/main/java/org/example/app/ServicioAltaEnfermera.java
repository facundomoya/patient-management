package org.example.app;

import org.example.app.interfaces.RepositorioEnfermeras;
import org.example.domain.Enfermera;
import org.example.auth.app.ServicioAuth;


public class ServicioAltaEnfermera {
    
    private final RepositorioEnfermeras repoEnfermeras;
    private final ServicioAuth servicioAuth;

    public ServicioAltaEnfermera(RepositorioEnfermeras repoEnfermeras) {
        this.repoEnfermeras = repoEnfermeras;
        this.servicioAuth = null;
    }

    public ServicioAltaEnfermera(RepositorioEnfermeras repoEnfermeras, ServicioAuth servicioAuth) {
        this.repoEnfermeras = repoEnfermeras;
        this.servicioAuth = servicioAuth;
    }

    public Enfermera registrarEnfermera(
            String cuil,
            String nombre,
            String apellido) {
        Enfermera enfermera = new Enfermera(nombre, apellido, cuil);
        repoEnfermeras.guardarEnfermera(enfermera);

        if (this.servicioAuth != null) {
            try {
                servicioAuth.inicializarUnUsuario(cuil);
            } catch (Exception e) {
                System.err.println("No se pudo inicializar usuario auth para enfermera " + cuil + ": " + e.getMessage());
            }
        }

        return enfermera;
    }
}
