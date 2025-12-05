package org.example.web.dto;

public class MedicoDTO {
    private String cuil;
    private String nombre;
    private String apellido;

    public MedicoDTO() {}

    public MedicoDTO(String cuil, String nombre, String apellido) {
        this.cuil = cuil;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public String getCuil() {
        return cuil;
    }

    public void setCuil(String cuil) {
        this.cuil = cuil;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}

