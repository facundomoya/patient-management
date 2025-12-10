package org.example.app;

import org.example.app.interfaces.RepositorioIngresos;
import org.example.app.interfaces.RepositorioPacientes;
import org.example.domain.Enfermera;
import org.example.domain.Ingreso;
import org.example.domain.NivelEmergencia;
import org.example.domain.Paciente;

//import java.util.ArrayList;
//import java.util.Comparator;
import java.util.List;

public class ServicioUrgencias {

    //SEGREGACION DE INTERFAZ
    //PATRON ADAPTADOR
    private RepositorioPacientes dbPacientes;
    private final RepositorioIngresos dbIngresos;

    //private final List<Ingreso> listaEspera;

    //INYECCION DE DEPENDENCIA -> Pruebas
    public ServicioUrgencias(RepositorioPacientes dbPacientes, RepositorioIngresos dbIngresos) {
        this.dbPacientes = dbPacientes;
        this.dbIngresos = dbIngresos;
        //this.listaEspera = new ArrayList<>();
    }


    public void registrarUrgencia(String cuilPaciente,
                                  Enfermera enfermera,
                                  String informe,
                                  NivelEmergencia emergencia,
                                  Float temperatura,
                                  Float frecuenciaCardiaca,
                                  Float frecuenciaRespiratoria,
                                  Float frecuenciaSistolica,
                                  Float frecuenciaDiastolica){
        Paciente paciente = dbPacientes.buscarPacientePorCuil(cuilPaciente)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        boolean yaTieneIngresoPendiente = dbIngresos.obtenerPendientes().stream()
                .anyMatch(i -> i.getPaciente().getCuil().equals(paciente.getCuil()));
        if (yaTieneIngresoPendiente) {
            // Podés usar tu DomainException si la tenés
            throw new RuntimeException("El paciente ya tiene una urgencia/ingreso pendiente");
        }
        boolean yaTieneIngresoEnProceso = dbIngresos.obtenerEnProceso().stream()
                .anyMatch(i -> i.getPaciente().getCuil().equals(paciente.getCuil()));
        if (yaTieneIngresoEnProceso) {
            // Podés usar tu DomainException si la tenés
            throw new RuntimeException("El paciente ya tiene una urgencia/ingreso en proceso");
        }
        
        Ingreso ingreso = new Ingreso(
                paciente,
                enfermera,
                informe,
                emergencia,
                temperatura,
                frecuenciaCardiaca,
                frecuenciaRespiratoria,
                frecuenciaSistolica,
                frecuenciaDiastolica);

        //listaEspera.add(ingreso);
        //listaEspera.sort(Ingreso::compareTo);

        dbIngresos.guardar(ingreso);
    }

    public List<Ingreso> obtenerIngresosPendientes(){
        return dbIngresos.obtenerPendientes();
    }

    public List<Ingreso> obtenerTodosLosIngresos() {
        return dbIngresos.obtenerTodos();
    }
}
