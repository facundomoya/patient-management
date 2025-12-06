package org.example.domain;

import java.time.LocalDateTime;
import org.example.app.interfaces.RepositorioIngresos;
import org.example.domain.Exceptions.DomainException;

public class Atencion {
    private Ingreso ingreso;
    private String informe;
    private Medico medico;
    private LocalDateTime fechaAtencion;

    public Atencion(Ingreso ingreso, String informe, Medico medico, LocalDateTime fechaAtencion) {
        // Validaciones mandatorias 
        if (ingreso == null) throw new DomainException("El ingreso es obligatorio");
        if (medico == null) throw new DomainException("El médico es obligatorio");
        if (informe == null || informe.isBlank()) {
            // Criterio de aceptación: Error si se omite informe 
            throw new DomainException("El informe de la atención se ha omitido");
        }

        this.ingreso = ingreso;
        this.medico = medico;
        this.informe = informe;
        this.fechaAtencion = LocalDateTime.now();
    }

    public Ingreso getIngreso() {
        return ingreso;
    }
    public String getInforme() {
        return informe;
    }
    public Medico getMedico() {
        return medico;
    }
}
