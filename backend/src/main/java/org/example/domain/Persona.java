package org.example.domain;



public class Persona {
    private String cuil;
    private String nombre;
    private String apellido;

    public Persona(String cuil, String nombre, String apellido) {
        this.cuil     = Guard.cuilValido(cuil, "El CUIL del paciente no puede ser nulo, en blanco ni inválido");
        this.nombre   = Guard.notBlank(nombre, "El nombre del paciente no puede ser nulo ni estar en blanco");
        this.apellido = Guard.notBlank(apellido, "El apellido del paciente no puede ser nulo ni estar en blanco");
    }

    public String getCuil() {
        return cuil;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }
}
