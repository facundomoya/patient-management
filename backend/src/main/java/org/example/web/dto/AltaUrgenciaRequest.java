
package org.example.web.dto;

public class AltaUrgenciaRequest {

    private String cuilPaciente;

    private String informe;
    private String nivelEmergencia;

    private Float temperatura;   
    private Float frecuenciaCardiaca;
    private Float frecuenciaRespiratoria;
    private Float tensionSistolica;
    private Float tensionDiastolica;

    public AltaUrgenciaRequest() {
    }

    public String getCuilPaciente() {
        return cuilPaciente;
    }

    public void setCuilPaciente(String cuilPaciente) {
        this.cuilPaciente = cuilPaciente;
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
