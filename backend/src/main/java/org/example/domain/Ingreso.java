package org.example.domain;

import java.time.LocalDateTime;

import org.example.domain.valueobject.FrecuenciaCardiaca;
import org.example.domain.valueobject.FrecuenciaRespiratoria;
import org.example.domain.valueobject.TensionArterial;

public class Ingreso implements Comparable<Ingreso> {

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
            Float frecuenciaDiastolica) {

        this.paciente = paciente;
        this.enfermera = enfermera;
        this.fechaIngreso = LocalDateTime.now();
        this.informe = informe;
        this.nivelEmergencia = nivelEmergencia;
        this.estado = EstadoIngreso.PENDIENTE;
        this.temperatura = temperatura;

        Float fCardiacaValidada = Guard.notNull(frecuenciaCardiaca, "La frecuencia cardíaca no puede ser nula");
        this.frecuenciaCardiaca = new FrecuenciaCardiaca(fCardiacaValidada);

        Float fRespiratoriaValidada = Guard.notNull(frecuenciaRespiratoria, "La frecuencia respiratoria no puede ser nula");
        this.frecuenciaRespiratoria = new FrecuenciaRespiratoria(fRespiratoriaValidada);

        Float fSistolicaValidada = Guard.notNull(frecuenciaSistolica, "La frecuencia sistólica no puede ser nula");
        Float fDiastolicaValidada = Guard.notNull(frecuenciaDiastolica, "La frecuencia diastólica no puede ser nula");
        this.tensionArterial = new TensionArterial(fSistolicaValidada, fDiastolicaValidada);
    }

    public String getCuilPaciente() {
        return this.paciente.getCuil();
    }

    public void setEstado(EstadoIngreso estado) {
        this.estado = estado;
    }

    @Override
    public int compareTo(Ingreso o) {
        int porNivel = this.nivelEmergencia.compararCon(o.nivelEmergencia); // usa tu compararCon
        if (porNivel != 0) {
            return porNivel;
        }
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

    public FrecuenciaCardiaca getFrecuenciaCardiacaVO() {
        return frecuenciaCardiaca;
    }

    public String getFrecuenciaCardiaca() {
        return frecuenciaCardiaca.getValorFormateado();
    }

    public FrecuenciaRespiratoria getFrecuenciaRespiratoriaVO() {
        return frecuenciaRespiratoria;
    }

    public String getFrecuenciaRespiratoria() {
        return frecuenciaRespiratoria.getValorFormateado();
    }

    public TensionArterial getTensionArterialVO() {
        return tensionArterial;
    }

    public String getTensionSistolica() {
        return tensionArterial.getSistolica();
    }

    public String getTensionDiastolica() {
        return tensionArterial.getDiastolica();
    }

}
