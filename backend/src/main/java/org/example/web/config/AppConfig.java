package org.example.web.config;

import org.example.app.AltaEnfermeraService;
import org.example.app.AltaMedicoService;
import org.example.app.AltaPacienteService;
import org.example.app.ServicioUrgencias;
import org.example.app.ModuloReclamo;
import org.example.app.ModuloRegistroAtencion;
import org.example.app.interfaces.RepositorioIngresos;
import org.example.app.interfaces.RepositorioObrasSociales;
import org.example.app.interfaces.RepositorioPacientes;
import org.example.app.interfaces.RepositorioAtenciones;
import org.example.app.interfaces.RepositorioEnfermeras;
import org.example.app.interfaces.RepositorioMedicos;
import org.example.auth.BCryptHasher;
import org.example.auth.app.ServicioAuth;
import org.example.auth.ports.PasswordHasher;
import org.example.auth.ports.UsuarioRepositorio;
import org.example.infrastructure.*;
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
    public RepositorioIngresos repositorioIngresos() {
        return new RepositorioIngresosEnMemoria();
    }

    @Bean
    public RepositorioAtenciones repositorioAtenciones() {
        return new RepositorioAtencionesEnMemoria();
    }

    @Bean
    public RepositorioEnfermeras repositorioEnfermeras() {
        // Implementación en memoria
        return new RepositorioEnfermerasEnMemoria();
    }

    @Bean
    public RepositorioMedicos repositorioMedicos() {
        // Implementación en memoria
        return new RepositorioMedicosEnMemoria();
    }

    @Bean
    public UsuarioRepositorio repositorioUsuarios() {
        return new RepositorioUsuariosEnMemoria();
    }

    // @Bean
    // public EnfermeraRepositorio enfermeraRepositorio(RepositorioEnfermeras repoEnfermeras) {
    //     return new EnfermeraRepositorioAdapter(repoEnfermeras);
    // }

    // @Bean
    // public MedicoRepositorio medicoRepositorio(RepositorioMedicos repoMedicos) {
    //     return new MedicoRepositorioAdapter(repoMedicos);
    // }

    @Bean
    public PasswordHasher passwordHasher() {
        // Usar logRounds bajo para desarrollo (más rápido)
        return new BCryptHasher(4);
    }

    @Bean
    public ServicioAuth servicioAuth(
            UsuarioRepositorio repoUsuarios,
            PasswordHasher hasher,
            RepositorioEnfermeras enfRepo,
            RepositorioMedicos medRepo
    ) {
        return new ServicioAuth(repoUsuarios, hasher, enfRepo, medRepo);
    }

    @Bean
    public InicializadorUsuarios inicializadorUsuarios(
            ServicioAuth servicioAuth,
            RepositorioEnfermeras repoEnfermeras,
            RepositorioMedicos repoMedicos
    ) {
        return new InicializadorUsuarios(servicioAuth, repoEnfermeras, repoMedicos);
    }

    @Bean
    public AltaPacienteService altaPacienteService(
            RepositorioPacientes repoPacientes,
            RepositorioObrasSociales repoOS
    ) {
        return new AltaPacienteService(repoPacientes, repoOS);
    }

    @Bean
    public ServicioUrgencias servicioUrgencias(RepositorioPacientes repoPacientes, RepositorioIngresos repoIngresos){
     return new ServicioUrgencias(repoPacientes, repoIngresos);
    }

    @Bean
    public ModuloReclamo moduloReclamo(RepositorioIngresos repoIngresos) {
        return new ModuloReclamo(repoIngresos);
    }

    @Bean
    public ModuloRegistroAtencion moduloRegistroAtencion(RepositorioAtenciones repoAtenciones, RepositorioIngresos repoIngresos) {
        return new ModuloRegistroAtencion(repoAtenciones, repoIngresos);
    }

    @Bean
    public AltaEnfermeraService altaEnfermeraService(
            RepositorioEnfermeras repoEnfermeras
    ) {
        return new AltaEnfermeraService(repoEnfermeras);
    }

    @Bean
    public AltaMedicoService altaMedicoService(
            RepositorioMedicos repoMedicos
    ) {
        return new AltaMedicoService(repoMedicos);
    }
}
