package org.example.app.interfaces;

import java.util.List;

import org.example.domain.Ingreso;

public interface RepositorioIngresos {
    void guardar(Ingreso ingreso);

    List<Ingreso> obtenerPendientes(); // Devuelve la lista ordenada
    
    void eliminarDePendientes(Ingreso ingreso);
}
