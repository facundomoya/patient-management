package org.example.app;

import java.time.LocalDateTime;

import org.example.app.interfaces.RepositorioAtenciones;
import org.example.app.interfaces.RepositorioIngresos;
import org.example.domain.*;

public class ServicioRegistroAtencion {
    private final RepositorioAtenciones dbAtenciones;
    private final RepositorioIngresos dbIngresos;

    public ServicioRegistroAtencion(RepositorioAtenciones dbAtenciones, RepositorioIngresos dbIngresos) {
        this.dbAtenciones = dbAtenciones;
        this.dbIngresos = dbIngresos;
    }

    public Atencion registrarAtencion(Ingreso ingreso,
                                  Medico medico,
                                  String informe,
                                  LocalDateTime fechaAtencion) {
        Atencion atencion = dbAtenciones.obtenerAtencionActivaPorMedico(medico)
                .filter(a -> a.getIngreso() == ingreso)
                .orElse(null);

        if (atencion != null) {
            atencion.setInforme(informe);
            atencion.setFechaAtencion(fechaAtencion);
        } else {
            atencion = new Atencion(ingreso, informe, medico, fechaAtencion);
            dbAtenciones.guardar(atencion);
        }

        ingreso.setEstado(EstadoIngreso.FINALIZADO);

        //dbIngresos.guardar(ingreso);

        return atencion;
    }
}
