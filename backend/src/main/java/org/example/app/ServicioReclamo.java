package org.example.app;

import java.time.LocalDateTime;
import java.util.List;

import org.example.app.interfaces.RepositorioIngresos;
import org.example.app.interfaces.RepositorioAtenciones;
import org.example.domain.EstadoIngreso;
import org.example.domain.Ingreso;
import org.example.domain.Medico;
import org.example.domain.Atencion;

public class ServicioReclamo {

    private final RepositorioIngresos dbIngresos;
    private final RepositorioAtenciones dbAtenciones;

    public ServicioReclamo(RepositorioIngresos dbIngresos, RepositorioAtenciones dbAtenciones) {
        this.dbIngresos = dbIngresos;
        this.dbAtenciones = dbAtenciones;
    }

    public Ingreso reclamarSiguientePaciente(Medico medico) {
        List<Ingreso> pendientes = dbIngresos.obtenerPendientes();

        if (pendientes.isEmpty()) {
            throw new RuntimeException("No hay pacientes en espera");
        }

        // Obtiene el primero (ya están ordenados por el repositorio)
        Ingreso siguiente = pendientes.get(0);

        // Cambiar estado del ingreso a EN_PROCESO
        siguiente.setEstado(EstadoIngreso.EN_PROCESO);
        //dbIngresos.guardar(siguiente);

        String informe = "Reclamado - Ingreso (En proceso)";
        // Crear Atencion con solo ingreso y medico (informe por defecto "Reclamado")
        Atencion atencion = new Atencion(siguiente, informe, medico, LocalDateTime.now());
        dbAtenciones.guardar(atencion);

        return siguiente;
    }

}