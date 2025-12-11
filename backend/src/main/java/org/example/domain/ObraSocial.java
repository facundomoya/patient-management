package org.example.domain;

public class ObraSocial {
    private String codigo;
    private String nombre;

    public ObraSocial(String codigo, String nombre) {
        this.codigo = Guard.notBlank(codigo, "El código de la obra social es obligatorio");
        this.nombre = Guard.notBlank(nombre, "El nombre de la obra social es obligatorio");
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
}
