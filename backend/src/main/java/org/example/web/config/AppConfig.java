package org.example.web.config;

import org.example.app.ServicioAltaEnfermera;
import org.example.app.ServicioAltaMedico;
import org.example.app.ServicioAltaPaciente;
import org.example.app.ServicioUrgencias;
import org.example.app.ServicioReclamo;
import org.example.app.ServicioRegistroAtencion;
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
        // ImplementaciÃ³n en memoria
        return new RepositorioPacientesEnMemoria();
    }

    @Bean
    public RepositorioObrasSociales repositorioObrasSociales() {
        // ImplementaciÃ³n en memoria
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
        // ImplementaciÃ³n en memoria
        return new RepositorioEnfermerasEnMemoria();
    }

    @Bean
    public RepositorioMedicos repositorioMedicos() {
        // ImplementaciÃ³n en memoria
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
        // Usar logRounds bajo para desarrollo (mÃ¡s rÃ¡pido)
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
    public ServicioAltaPaciente altaPacienteService(
            RepositorioPacientes repoPacientes,
            RepositorioObrasSociales repoOS
    ) {
        return new ServicioAltaPaciente(repoPacientes, repoOS);
    }

    @Bean
    public ServicioUrgencias servicioUrgencias(RepositorioPacientes repoPacientes, RepositorioIngresos repoIngresos){
     return new ServicioUrgencias(repoPacientes, repoIngresos);
    }

    @Bean
    public ServicioReclamo moduloReclamo(RepositorioIngresos repoIngresos, RepositorioAtenciones repoAtenciones) {
        return new ServicioReclamo(repoIngresos, repoAtenciones);
    }

    @Bean
    public ServicioRegistroAtencion moduloRegistroAtencion(RepositorioAtenciones repoAtenciones, RepositorioIngresos repoIngresos) {
        return new ServicioRegistroAtencion(repoAtenciones, repoIngresos);
    }

    @Bean
    public ServicioAltaEnfermera altaEnfermeraService(
            RepositorioEnfermeras repoEnfermeras,
            ServicioAuth servicioAuth
    ) {
        return new ServicioAltaEnfermera(repoEnfermeras, servicioAuth);
    }

    @Bean
    public ServicioAltaMedico altaMedicoService(
            RepositorioMedicos repoMedicos,
            ServicioAuth servicioAuth
    ) {
        return new ServicioAltaMedico(repoMedicos, servicioAuth);
    }
}

