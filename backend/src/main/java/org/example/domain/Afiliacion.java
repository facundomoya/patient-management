package org.example.domain;
import org.example.domain.Exceptions.DomainException;


public class Afiliacion {
    private ObraSocial obraSocial;      
    private String numeroAfiliado;     

    public Afiliacion(ObraSocial obraSocial, String numeroAfiliado) {
        if (obraSocial == null) throw DomainException.validation("La obra social del afiliado es obligatoria");
        this.obraSocial = obraSocial;
        this.numeroAfiliado = Guard.notBlank(numeroAfiliado, "El número de afiliado es obligatorio");
    }

    public ObraSocial getObraSocial() { return obraSocial; }
    public String getNumeroAfiliado() { return numeroAfiliado; }
}
