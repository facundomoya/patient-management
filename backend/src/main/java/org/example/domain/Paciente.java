package org.example.domain;

public class Paciente extends Persona {
    private Domicilio domicilio;  
    private Afiliacion afiliacion;

    // Sin afiliación
    public Paciente(String cuil, String nombre, String apellido, Domicilio domicilio) {
        super(cuil, nombre, apellido);
        this.domicilio = domicilio == null
                ? null : domicilio;
        if (this.domicilio == null)
            throw org.example.domain.Exceptions.DomainException.validation("El domicilio del paciente es obligatorio");
        this.afiliacion = null;
    }

    // Con afiliación opcional
    public Paciente(String cuil, String nombre, String apellido, Domicilio domicilio, Afiliacion afiliacion) {
        this(cuil, nombre, apellido, domicilio);
        this.afiliacion = afiliacion;
    }


    public Domicilio getDomicilio() { return domicilio; }
    public Afiliacion getAfiliacion() { return afiliacion; }
    public boolean tieneAfiliacion() { return afiliacion != null; }
}
