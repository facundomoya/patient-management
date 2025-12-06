package org.example.app.interfaces;

import java.util.List;

import org.example.domain.Atencion;

public interface RepositorioAtenciones {
    void guardar(Atencion atencion);

    List<Atencion> obtenerAtenciones();
}
