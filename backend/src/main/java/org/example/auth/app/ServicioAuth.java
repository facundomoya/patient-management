package org.example.auth.app;

import org.example.app.interfaces.RepositorioEnfermeras;
import org.example.app.interfaces.RepositorioMedicos;
import org.example.auth.domain.Usuario;
import org.example.auth.ports.*;
import org.example.domain.Enfermera;
import org.example.domain.Medico;
import org.example.domain.Exceptions.DomainException;

import java.util.Optional;
import java.util.regex.Pattern;

public class ServicioAuth {
    private static final String LOGIN_ERROR = "Usuario o contraseña inválidos";
    private static final Pattern EMAIL_RE =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,63}$");

    private final UsuarioRepositorio usuarios;
    private final PasswordHasher hasher;
    private final RepositorioEnfermeras enfRepo;
    private final RepositorioMedicos medRepo;

    public ServicioAuth(UsuarioRepositorio usuarios,
                        PasswordHasher hasher,
                        RepositorioEnfermeras enfRepo,
                        RepositorioMedicos medRepo) {
        this.usuarios = usuarios;
        this.hasher = hasher;
        this.enfRepo = enfRepo;
        this.medRepo = medRepo;
    }

    public Usuario registrarParaEnfermera(String email, String password, String cuilEnfermera) {
        validarEmail(email);
        validarPassword(password);
        validarCuil(cuilEnfermera);

        Enfermera e = enfRepo.buscarEnfermeraPorCuil(cuilEnfermera)
                .orElseThrow(() -> DomainException.validation("Enfermera inexistente"));

        if (usuarios.existePorEnfermera(cuilEnfermera)) {
            throw DomainException.business("1","El actor ya tiene un usuario asociado");
        }

        String hash = hasher.hash(password);
        Usuario u = Usuario.paraEnfermera(email, hash, e);
        return usuarios.guardar(u);
    }

    public Usuario registrarParaMedico(String email, String password, String cuilMedico) {
        validarEmail(email);
        validarPassword(password);

        Medico m = medRepo.buscarMedicoPorCuil(cuilMedico)
                .orElseThrow(() -> DomainException.validation("Médico inexistente"));

        if (usuarios.existePorMedico(cuilMedico)) {
            throw DomainException.business("2","El actor ya tiene un usuario asociado");
        }

        String hash = hasher.hash(password);
        Usuario u = Usuario.paraMedico(email, hash, m);
        return usuarios.guardar(u);
    }

    public void inicializarUnUsuario(String cuil) {
        try {
            Optional<Enfermera> enfermeraOpt = enfRepo.buscarEnfermeraPorCuil(cuil);
            if (enfermeraOpt.isPresent()) {
                Enfermera enfermera = enfermeraOpt.get();
                String digits = enfermera.getCuil().replaceAll("[^0-9]", "");
                String email = "enfermera" + digits + "@clinica.com";
                try {
                    registrarParaEnfermera(email, "password123", enfermera.getCuil());
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            System.err.println("Error buscando/enregistrando enfermera para cuil " + cuil + ": " + e.getMessage());
        }

        try {
            Optional<Medico> medicoOpt = medRepo.buscarMedicoPorCuil(cuil);
            if (medicoOpt.isPresent()) {
                Medico medico = medicoOpt.get();
                String digits = medico.getCuil().replaceAll("[^0-9]", "");
                String email = "medico" + digits + "@clinica.com";
                try {
                    registrarParaMedico(email, "password123", medico.getCuil());
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            System.err.println("Error buscando/enregistrando medico para cuil " + cuil + ": " + e.getMessage());
        }
    }

    public Usuario login(String email, String password) {
        return usuarios.buscarPorEmail(email)
                .filter(u -> hasher.matches(password, u.getHash()))
                .orElseThrow(() -> DomainException.business("3",LOGIN_ERROR));
    }

    private static void validarEmail(String email) {
        if (email == null || email.isBlank() || !EMAIL_RE.matcher(email).matches()) {
            throw DomainException.validation("Email inválido").withContext("valor", email);
        }
    }
    private static void validarPassword(String password) {
        if (password == null || password.isBlank())
            throw DomainException.validation("Contraseña es obligatoria");
        if (password.length() < 8)
            throw DomainException.validation("La contraseña debe tener al menos 8 caracteres");
    }
    private static void validarCuil(String cuil) {
        if (cuil == null || cuil.isBlank())
            throw DomainException.validation("CUIL es obligatorio");
    }
}

