package org.example.app;

import org.example.app.interfaces.RepositorioPacientes;

import java.util.List;

import org.example.app.interfaces.RepositorioObrasSociales;
import org.example.domain.*;
import org.example.domain.Exceptions.DomainException;

public class  ServicioAltaPaciente {

    private final RepositorioPacientes repoPacientes;
    private final RepositorioObrasSociales repoOS;

    public ServicioAltaPaciente(RepositorioPacientes repoPacientes, RepositorioObrasSociales repoOS) {
        this.repoPacientes = repoPacientes;
        this.repoOS = repoOS;
    }

    public Paciente registrarPaciente(
            String cuil, String nombre, String apellido,
            String calle, Integer numero, String localidad,
            String obraSocialCodigo, String numeroAfiliado // ambos opcionales en la API
    ) {
        // Mandatorios del paciente (dominio validará cada campo)
        Domicilio domicilio = new Domicilio(calle, numero, localidad);

        // Sin obra social: se crea sin afiliación
        if (isBlank(obraSocialCodigo)) {
            Paciente p = new Paciente(cuil, nombre, apellido, domicilio);
            repoPacientes.guardarPaciente(p);
            return p;
        }

        // Con obra social: debe EXISTIR en catálogo
        ObraSocial os = repoOS.buscarPorCodigo(obraSocialCodigo)
                .orElseThrow(() -> DomainException.validation(
                        "No se puede registrar al paciente con una obra social inexistente: " + obraSocialCodigo));

        // Debe estar AFILIADO (numeroAfiliado mandatorio si vino OS)
        if (isBlank(numeroAfiliado)) {
            throw DomainException.validation(
                    "No se puede registrar al paciente dado que no está afiliado a la obra social " + obraSocialCodigo);
        }

        Afiliacion afiliacion = new Afiliacion(os, numeroAfiliado);
        Paciente p = new Paciente(cuil, nombre, apellido, domicilio, afiliacion);
        repoPacientes.guardarPaciente(p);
        return p;
    }

    public List<Paciente> listarPacientes() {
        return repoPacientes.listarTodos();
    }

    private static boolean isBlank(String s) { return s == null || s.isBlank(); }
}
