package org.example.web.config;

import org.example.app.interfaces.RepositorioEnfermeras;
import org.example.app.interfaces.RepositorioMedicos;
import org.example.auth.app.ServicioAuth;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class InicializadorUsuarios implements InitializingBean {

    private final ServicioAuth servicioAuth;
    private final RepositorioEnfermeras repoEnfermeras;
    private final RepositorioMedicos repoMedicos;

    public InicializadorUsuarios(ServicioAuth servicioAuth,
                                 RepositorioEnfermeras repoEnfermeras,
                                 RepositorioMedicos repoMedicos) {
        this.servicioAuth = servicioAuth;
        this.repoEnfermeras = repoEnfermeras;
        this.repoMedicos = repoMedicos;
    }

    @Override
    public void afterPropertiesSet() {
        inicializarUsuarios();
    }

    private void inicializarUsuarios() {
        try {
            // Obtener enfermeras existentes y crear usuarios
            repoEnfermeras.listarTodas().forEach(enfermera -> {
                try {
                    String email = "enfermera" + enfermera.getCuil().replaceAll("[^0-9]", "") + "@clinica.com";
                    servicioAuth.registrarParaEnfermera(email, "password123", enfermera.getCuil());
                } catch (Exception e) {
                    // Ignorar si ya existe el usuario
                }
            });

            // Obtener médicos existentes y crear usuarios
            repoMedicos.listarTodos().forEach(medico -> {
                try {
                    String email = "medico" + medico.getCuil().replaceAll("[^0-9]", "") + "@clinica.com";
                    servicioAuth.registrarParaMedico(email, "password123", medico.getCuil());
                } catch (Exception e) {
                    // Ignorar si ya existe el usuario
                }
            });
        } catch (Exception e) {
            // Si falla, continuar sin usuarios de prueba
            System.err.println("Error al inicializar usuarios: " + e.getMessage());
        }
    }
}

