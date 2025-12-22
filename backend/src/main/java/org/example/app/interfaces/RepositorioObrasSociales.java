// src/main/java/org/example/app/interfaces/RepositorioObrasSociales.java
package org.example.app.interfaces;

import org.example.domain.ObraSocial;

import java.util.List;
import java.util.Optional;

public interface RepositorioObrasSociales {

    Optional<ObraSocial> buscarPorCodigo(String codigo);

    List<ObraSocial> listarTodas();

    void guardar(ObraSocial obraSocial);
}
