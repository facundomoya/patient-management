package org.example.web.config;

import org.example.app.AltaPacienteService;
import org.example.app.ServicioUrgencias;
import org.example.app.interfaces.RepositorioObrasSociales;
import org.example.app.interfaces.RepositorioPacientes;
import org.example.infrastructure.RepositorioObrasSocialesEnMemoria;
import org.example.infrastructure.RepositorioPacientesEnMemoria;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public RepositorioPacientes repositorioPacientes() {
        // Implementación en memoria
        return new RepositorioPacientesEnMemoria();
    }

    @Bean
    public RepositorioObrasSociales repositorioObrasSociales() {
        // Implementación en memoria
        return new RepositorioObrasSocialesEnMemoria();
    }

    @Bean
    public AltaPacienteService altaPacienteService(
            RepositorioPacientes repoPacientes,
            RepositorioObrasSociales repoOS
    ) {
        return new AltaPacienteService(repoPacientes, repoOS);
    }

    @Bean
    public ServicioUrgencias servicioUrgencias(RepositorioPacientes repoPacientes){
     return new ServicioUrgencias(repoPacientes);
    }
}
