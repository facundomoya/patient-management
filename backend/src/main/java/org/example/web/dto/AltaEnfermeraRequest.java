package org.example.web.dto;

public class AltaEnfermeraRequest {
    private String cuil;
    private String nombre;
    private String apellido;

    public AltaEnfermeraRequest() {}

    public String getCuil() { return cuil; }
    public void setCuil(String cuil) { this.cuil = cuil; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
}


