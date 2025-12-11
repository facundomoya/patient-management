package org.example.auth.domain;

import org.example.domain.Enfermera;
import org.example.domain.Guard;
import org.example.domain.Medico;
import org.example.domain.Exceptions.DomainException;

import java.util.Objects;

public class Usuario {
    private final String email;
    private final String hash;        // contraseña hasheada
    private final Enfermera enfermera;
    private final Medico medico;      


    Usuario(String email, String hash, Enfermera enfermera, Medico medico) {
        if ((enfermera == null) == (medico == null)) {
            throw DomainException.validation(
                    "Usuario debe asociarse a médico o enfermera (exactamente uno)");
        }
        this.email = Guard.requireEmailValido(email);
        this.hash  = Guard.notBlank(hash, "Hash de contraseña requerido");
        this.enfermera = enfermera;
        this.medico = medico;
    }

    public static Usuario paraEnfermera(String email, String hash, Enfermera e) {
        return new Usuario(email, hash, Objects.requireNonNull(e), null);
    }
    public static Usuario paraMedico(String email, String hash, Medico m) {
        return new Usuario(email, hash, null, Objects.requireNonNull(m));
    }

    public String getEmail() { return email; }
    public String getHash()  { return hash;  }

    public boolean esEnfermera() { return enfermera != null; }
    public boolean esMedico()    { return medico != null; }

    public Enfermera getEnfermera() {
        if (!esEnfermera()) throw new IllegalStateException("Usuario no es enfermera");
        return enfermera;
    }
    public Medico getMedico() {
        if (!esMedico()) throw new IllegalStateException("Usuario no es médico");
        return medico;
    }

    public String getRol() { return esMedico() ? "MEDICO" : "ENFERMERA"; }
    public String getCuilActor() { return esMedico() ? medico.getCuil() : enfermera.getCuil(); }


}
