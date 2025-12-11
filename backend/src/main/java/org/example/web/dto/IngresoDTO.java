
package org.example.web.dto;

public class IngresoDTO {

    private String cuilPaciente;
    private String nombrePaciente;
    private String apellidoPaciente;

    private String nivelEmergencia;
    private String informe;

    private Float temperatura;
    private String frecuenciaCardiaca;
    private String frecuenciaRespiratoria; 
    private String tensionSistolica; 
    private String tensionDiastolica;
    private String estado;
    private String fechaIngreso;
    private String enfermeroCuil;

    public IngresoDTO() {
    }

    // getters y setters

    public String getCuilPaciente() {
        return cuilPaciente;
    }

    public void setCuilPaciente(String cuilPaciente) {
        this.cuilPaciente = cuilPaciente;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getApellidoPaciente() {
        return apellidoPaciente;
    }

    public void setApellidoPaciente(String apellidoPaciente) {
        this.apellidoPaciente = apellidoPaciente;
    }

    public String getNivelEmergencia() {
        return nivelEmergencia;
    }

    public void setNivelEmergencia(String nivelEmergencia) {
        this.nivelEmergencia = nivelEmergencia;
    }

    public String getInforme() {
        return informe;
    }

    public void setInforme(String informe) {
        this.informe = informe;
    }

    public Float getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Float temperatura) {
        this.temperatura = temperatura;
    }

    public String getFrecuenciaCardiaca() {
        return frecuenciaCardiaca;
    }

    public void setFrecuenciaCardiaca(String frecuenciaCardiaca) {
        this.frecuenciaCardiaca = frecuenciaCardiaca;
    }

    public String getFrecuenciaRespiratoria() {
        return frecuenciaRespiratoria;
    }

    public void setFrecuenciaRespiratoria(String frecuenciaRespiratoria) {
        this.frecuenciaRespiratoria = frecuenciaRespiratoria;
    }

    public String getTensionSistolica() {
        return tensionSistolica;
    }

    public void setTensionSistolica(String  tensionSistolica) {
        this.tensionSistolica = tensionSistolica;
    }

    public String getTensionDiastolica() {
        return tensionDiastolica;
    }

    public void setTensionDiastolica(String tensionDiastolica) {
        this.tensionDiastolica = tensionDiastolica;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getEnfermeroCuil() {
        return enfermeroCuil;
    }

    public void setEnfermeroCuil(String enfermeroCuil) {
        this.enfermeroCuil = enfermeroCuil;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
