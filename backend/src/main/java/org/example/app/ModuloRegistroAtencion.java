package org.example.app;

import java.time.LocalDateTime;

import org.example.app.interfaces.RepositorioAtenciones;
import org.example.app.interfaces.RepositorioIngresos;
import org.example.domain.*;

public class ModuloRegistroAtencion {
    private final RepositorioAtenciones dbAtenciones;
    private final RepositorioIngresos dbIngresos;

    public ModuloRegistroAtencion(RepositorioAtenciones dbAtenciones, RepositorioIngresos dbIngresos) {
        this.dbAtenciones = dbAtenciones;
        this.dbIngresos = dbIngresos;
    }

    public Atencion registrarAtencion(Ingreso ingreso,
                                  Medico medico,
                                  String informe,
                                  LocalDateTime fechaAtencion) {
        Atencion atencion = new Atencion(ingreso, informe, medico, fechaAtencion);

        dbAtenciones.guardar(atencion);

        ingreso.setEstado(EstadoIngreso.FINALIZADO);

        dbIngresos.guardar(ingreso);

        return atencion;
    }
}
