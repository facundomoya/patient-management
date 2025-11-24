package org.example.domain;

import org.example.domain.valueobject.FrecuenciaCardiaca;
import org.example.domain.valueobject.FrecuenciaRespiratoria;
import org.example.domain.valueobject.TensionArterial;

import java.time.LocalDateTime;

public class Ingreso implements Comparable<Ingreso>{
    Paciente paciente;
    Enfermera enfermera;
    LocalDateTime fechaIngreso;
    String informe;
    NivelEmergencia nivelEmergencia;
    EstadoIngreso estado;
    Float temperatura;
    FrecuenciaCardiaca frecuenciaCardiaca;
    FrecuenciaRespiratoria frecuenciaRespiratoria;
    TensionArterial tensionArterial;

    public Ingreso(Paciente paciente,
                   Enfermera enfermera,
                   String informe,
                   NivelEmergencia nivelEmergencia,
                   Float temperatura,
                   Float frecuenciaCardiaca,
                   Float frecuenciaRespiratoria,
                   Float frecuenciaSistolica,
                   Float frecuenciaDiastolica){
        this.paciente = paciente;
        this.enfermera = enfermera;
        this.fechaIngreso = LocalDateTime.now();
        this.informe = informe;
        this.nivelEmergencia = nivelEmergencia;
        this.estado = EstadoIngreso.PENDIENTE;
        this.temperatura = temperatura;
        this.frecuenciaCardiaca = new FrecuenciaCardiaca(frecuenciaCardiaca);
        this.frecuenciaRespiratoria = new FrecuenciaRespiratoria(frecuenciaRespiratoria);
        this.tensionArterial = new TensionArterial(frecuenciaSistolica,frecuenciaDiastolica);
    }


    public String getCuilPaciente(){
        return this.paciente.getCuil();
    }

    @Override
    public int compareTo(Ingreso o) {
        int porNivel = this.nivelEmergencia.compararCon(o.nivelEmergencia); // usa tu compararCon
        if (porNivel != 0) return porNivel;
        return this.fechaIngreso.compareTo(o.fechaIngreso); // desempate por llegada
    }
    // ===== Getters de dominio =====

    public Paciente getPaciente() {
        return paciente;
    }

    public Enfermera getEnfermera() {
        return enfermera;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public String getInforme() {
        return informe;
    }

    public NivelEmergencia getNivelEmergencia() {
        return nivelEmergencia;
    }

    public EstadoIngreso getEstado() {
        return estado;
    }

    public Float getTemperatura() {
        return temperatura;
    }

    // Value object completo (por si lo necesitás)
    public FrecuenciaCardiaca getFrecuenciaCardiacaVO() {
        return frecuenciaCardiaca;
    }

    // Conveniencia para el mapper / JSON
    public String  getFrecuenciaCardiaca() {
        // ajustá el nombre del metodo según tu value object
        return frecuenciaCardiaca.getValorFormateado();
    }

    public FrecuenciaRespiratoria getFrecuenciaRespiratoriaVO() {
        return frecuenciaRespiratoria;
    }

    public String getFrecuenciaRespiratoria() {
        // idem, ajustá si tu VO usa otro nombre
        return frecuenciaRespiratoria.getValorFormateado();
    }

    public TensionArterial getTensionArterialVO() {
        return tensionArterial;
    }

    public String getTensionSistolica() {
        // ajustá a como se llamen los getters en TensionArterial
        return tensionArterial.getSistolica();
    }

    public String getTensionDiastolica() {
        return tensionArterial.getDiastolica();
    }

}
