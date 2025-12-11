package org.example.app;

import org.example.app.interfaces.RepositorioIngresos;
import org.example.app.interfaces.RepositorioPacientes;
import org.example.domain.Enfermera;
import org.example.domain.Ingreso;
import org.example.domain.NivelEmergencia;
import org.example.domain.Paciente;


import java.util.List;

public class ServicioUrgencias {

    private RepositorioPacientes dbPacientes;
    private final RepositorioIngresos dbIngresos;

    public ServicioUrgencias(RepositorioPacientes dbPacientes, RepositorioIngresos dbIngresos) {
        this.dbPacientes = dbPacientes;
        this.dbIngresos = dbIngresos;
        
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
           
            throw new RuntimeException("El paciente ya tiene una urgencia/ingreso pendiente");
        }
        boolean yaTieneIngresoEnProceso = dbIngresos.obtenerEnProceso().stream()
                .anyMatch(i -> i.getPaciente().getCuil().equals(paciente.getCuil()));
        if (yaTieneIngresoEnProceso) {
            
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


        dbIngresos.guardar(ingreso);
    }

    public List<Ingreso> obtenerIngresosPendientes(){
        return dbIngresos.obtenerPendientes();
    }

    public List<Ingreso> obtenerTodosLosIngresos() {
        return dbIngresos.obtenerTodos();
    }
}
