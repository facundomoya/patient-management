package org.example.infrastructure;

import org.example.app.interfaces.RepositorioPacientes;
import org.example.domain.Afiliacion;
import org.example.domain.Domicilio;
import org.example.domain.ObraSocial;
import org.example.domain.Paciente;

import java.util.*;

public class RepositorioPacientesEnMemoria implements RepositorioPacientes {

    private final Map<String, Paciente> pacientes = new HashMap<>();

    public RepositorioPacientesEnMemoria() {
        // Cargamos algunos pacientes de prueba al iniciar
        inicializarDatosMock();
    }

    private void inicializarDatosMock() {
        // ⚠ IMPORTANTE:
        // Asegurate de que estos CUILs respeten el formato que valida tu Guard.cuilValido(...)
        // Si tu Guard espera 11 dígitos sin guiones, usá "20123456789"
        // Si espera con guiones, usá "20-12345678-9", etc.

        // Paciente 1: con obra social
        Domicilio dom1 = new Domicilio("Av. Libertador", 1234, "San Miguel de Tucumán");
        ObraSocial os1 = new ObraSocial("OSDE", "OSDE");
        Afiliacion afil1 = new Afiliacion(os1, "12345678");
        Paciente p1 = new Paciente(
                "20-43336577-2",   // ajustá al formato que acepte tu Guard
                "Santiago Joaquin",
                "Martin Peñalosa",
                dom1,
                afil1
        );

        // Paciente 2: con obra social
        Domicilio dom2 = new Domicilio("Av. Peron", 1000, "Yerba Buena");
        ObraSocial os2 = new ObraSocial("OSDE", "OSDE");
        Afiliacion afil2 = new Afiliacion(os2, "11345668");
        Paciente p2 = new Paciente(
                "20-44580992-7",   
                "Juan Martin",
                "Bossi",
                dom2,
                afil2
        );
        guardarPaciente(p1);
        guardarPaciente(p2);
    }

    @Override
    public void guardarPaciente(Paciente paciente) {
        pacientes.put(paciente.getCuil(), paciente);
    }

    @Override
    public Optional<Paciente> buscarPacientePorCuil(String cuil) {
        return Optional.ofNullable(pacientes.get(cuil));
    }

    @Override
    public List<Paciente> listarTodos() {
        return new ArrayList<>(pacientes.values());
    }
}
