package org.example.app;

import java.util.List;

import org.example.app.interfaces.RepositorioIngresos;
import org.example.domain.EstadoIngreso;
import org.example.domain.Ingreso;

public class ModuloReclamo {

    private final RepositorioIngresos dbIngresos; // Usa el MISMO repositorio
    //private final RepositorioAtenciones dbAtenciones; // Nuevo repositorio para atenciones

    public ModuloReclamo(RepositorioIngresos dbIngresos) {
        this.dbIngresos = dbIngresos;
    }

    public Ingreso reclamarSiguientePaciente() {
        List<Ingreso> pendientes = dbIngresos.obtenerPendientes();

        if (pendientes.isEmpty()) {
            throw new RuntimeException("No hay pacientes en espera"); // Condicion de si la lista esta vacia
        }

        // Obtiene el primero (ya están ordenados por el repositorio)
        Ingreso siguiente = pendientes.get(0);

        // Lógica de negocio del médico
        siguiente.setEstado(EstadoIngreso.EN_PROCESO);
        
        // Guardar el cambio de estado
        dbIngresos.guardar(siguiente);

        return siguiente;
    }

    
}
