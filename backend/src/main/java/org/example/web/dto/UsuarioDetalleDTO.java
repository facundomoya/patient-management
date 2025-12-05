package org.example.web.dto;

public class UsuarioDetalleDTO {
    private String email;
    private String rol;
    private String cuilActor;
    private String nombre;
    private String apellido;
    private String hashPreview; // Primeros caracteres del hash para verificar que está hasheado

    public UsuarioDetalleDTO() {}

    public UsuarioDetalleDTO(String email, String rol, String cuilActor, String nombre, String apellido, String hashPreview) {
        this.email = email;
        this.rol = rol;
        this.cuilActor = cuilActor;
        this.nombre = nombre;
        this.apellido = apellido;
        this.hashPreview = hashPreview;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getCuilActor() {
        return cuilActor;
    }

    public void setCuilActor(String cuilActor) {
        this.cuilActor = cuilActor;
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

    public String getHashPreview() {
        return hashPreview;
    }

    public void setHashPreview(String hashPreview) {
        this.hashPreview = hashPreview;
    }
}

