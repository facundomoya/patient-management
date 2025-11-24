// src/main/java/org/example/web/dto/RegistrarUrgenciaRequest.java
package org.example.web.dto;

public class RegistrarUrgenciaRequest {

    private String cuilPaciente;

    private String enfermeraCuil;
    private String enfermeraNombre;
    private String enfermeraApellido;

    private String informe;
    private String nivelEmergencia; // por ejemplo: "Rojo", "Amarillo", etc. según tu enum

    private Float temperatura;           // puede ser null
    private Float frecuenciaCardiaca;
    private Float frecuenciaRespiratoria;
    private Float tensionSistolica;
    private Float tensionDiastolica;

    public RegistrarUrgenciaRequest() {
    }

    public String getCuilPaciente() {
        return cuilPaciente;
    }

    public void setCuilPaciente(String cuilPaciente) {
        this.cuilPaciente = cuilPaciente;
    }

    public String getEnfermeraCuil() {
        return enfermeraCuil;
    }

    public void setEnfermeraCuil(String enfermeraCuil) {
        this.enfermeraCuil = enfermeraCuil;
    }

    public String getEnfermeraNombre() {
        return enfermeraNombre;
    }

    public void setEnfermeraNombre(String enfermeraNombre) {
        this.enfermeraNombre = enfermeraNombre;
    }

    public String getEnfermeraApellido() {
        return enfermeraApellido;
    }

    public void setEnfermeraApellido(String enfermeraApellido) {
        this.enfermeraApellido = enfermeraApellido;
    }

    public String getInforme() {
        return informe;
    }

    public void setInforme(String informe) {
        this.informe = informe;
    }

    public String getNivelEmergencia() {
        return nivelEmergencia;
    }

    public void setNivelEmergencia(String nivelEmergencia) {
        this.nivelEmergencia = nivelEmergencia;
    }

    public Float getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Float temperatura) {
        this.temperatura = temperatura;
    }

    public Float getFrecuenciaCardiaca() {
        return frecuenciaCardiaca;
    }

    public void setFrecuenciaCardiaca(Float frecuenciaCardiaca) {
        this.frecuenciaCardiaca = frecuenciaCardiaca;
    }

    public Float getFrecuenciaRespiratoria() {
        return frecuenciaRespiratoria;
    }

    public void setFrecuenciaRespiratoria(Float frecuenciaRespiratoria) {
        this.frecuenciaRespiratoria = frecuenciaRespiratoria;
    }

    public Float getTensionSistolica() {
        return tensionSistolica;
    }

    public void setTensionSistolica(Float tensionSistolica) {
        this.tensionSistolica = tensionSistolica;
    }

    public Float getTensionDiastolica() {
        return tensionDiastolica;
    }

    public void setTensionDiastolica(Float tensionDiastolica) {
        this.tensionDiastolica = tensionDiastolica;
    }
}
