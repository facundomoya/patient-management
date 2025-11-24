package org.example.domain.valueobject;

public class TensionArterial {
    private FrecuenciaDiastolica frecuenciaDiastolica;
    private FrecuenciaSistolica frecuenciaSistolica;

    public TensionArterial(Float frecuenciaSistolica, Float frecuenciaDiastolica) {
        this.frecuenciaSistolica = new FrecuenciaSistolica(frecuenciaSistolica);
        this.frecuenciaDiastolica = new FrecuenciaDiastolica(frecuenciaDiastolica);
    }

    // ===== Getters de los value objects =====

    public FrecuenciaSistolica getFrecuenciaSistolicaVO() {
        return frecuenciaSistolica;
    }

    public FrecuenciaDiastolica getFrecuenciaDiastolicaVO() {
        return frecuenciaDiastolica;
    }

    // ===== Getters "planos" para usar desde Ingreso / mapper =====

    public String getSistolica() {
        return frecuenciaSistolica.getValorFormateado();
    }

    public String getDiastolica() {
        return frecuenciaDiastolica.getValorFormateado();
    }
}
