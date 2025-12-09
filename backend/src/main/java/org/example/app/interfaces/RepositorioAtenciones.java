package org.example.app.interfaces;

import java.util.List;
import java.util.Optional;

import org.example.domain.Atencion;
import org.example.domain.Medico;

public interface RepositorioAtenciones {
    void guardar(Atencion atencion);

    List<Atencion> obtenerAtenciones();

    Optional<Atencion> obtenerPorMedico(Medico medico);

    Optional<Atencion> obtenerAtencionActivaPorMedico(Medico medico);

    List<Atencion> obtenerFinalizadas();
}