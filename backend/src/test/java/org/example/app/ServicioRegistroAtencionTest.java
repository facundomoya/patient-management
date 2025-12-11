package org.example.app;

import java.time.LocalDateTime;

import org.example.domain.Atencion;
import org.example.domain.Domicilio;
import org.example.domain.Enfermera;
import org.example.domain.EstadoIngreso;
import org.example.domain.Ingreso;
import org.example.domain.Medico;
import org.example.domain.Paciente;
import org.example.domain.NivelEmergencia;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mock.DBAtencionEnMemoria;
import mock.DBIngresoEnMemoria;

public class ServicioRegistroAtencionTest {
    private DBAtencionEnMemoria dbAtencion;
    private DBIngresoEnMemoria dbIngreso;
    private ServicioRegistroAtencion moduloRegistroAtencion;
    private Medico medico;
    private Enfermera enfermera;
    private Paciente p1;

    private Ingreso ingreso1;


    @BeforeEach
    void setUp(){
        dbAtencion = new DBAtencionEnMemoria();
        dbIngreso = new DBIngresoEnMemoria();
        moduloRegistroAtencion = new ServicioRegistroAtencion(dbAtencion, dbIngreso);

        medico = new Medico("Dr", "House", "20-43139260-8");
        enfermera = new Enfermera("Lucia", "Paz", "20-32456878-7");
        p1 = nuevoPaciente("20-40902338-0", "Facundo", "Moya");
       

        ingreso1 = new Ingreso(p1, enfermera, "Paciente prueba 1", NivelEmergencia.CRITICA, 37.0f, 80f, 18f, 120f, 80f);
    }

    private Paciente nuevoPaciente(String cuil, String nombre, String apellido) {
        Domicilio dom = new Domicilio("San Martín", 1234, "Tucumán");
        Paciente p = new Paciente(cuil, nombre, apellido, dom);
        return p;
    }

    @Test
    void registrar_atencion_cambia_estado_a_finalizado_y_guarda() {
        ingreso1.setEstado(EstadoIngreso.EN_PROCESO);
        dbIngreso.guardar(ingreso1);

        String informeMedico = "Paciente estabilizado y derivado a planta.";
        LocalDateTime fechaAtencion = LocalDateTime.now();

        Atencion resultadoAtencion = moduloRegistroAtencion.registrarAtencion(ingreso1, medico, informeMedico, fechaAtencion);

        Assertions.assertEquals(EstadoIngreso.FINALIZADO, ingreso1.getEstado());

        Assertions.assertEquals(resultadoAtencion, dbAtencion.obtenerAtenciones().contains(resultadoAtencion) ? resultadoAtencion : null);

        Assertions.assertFalse(dbIngreso.obtenerPendientes().contains(ingreso1));

        Assertions.assertTrue(dbIngreso.obtenerFinalizados().contains(ingreso1));
    }

    @Test
    void registrar_atencion_con_informe_omitido() {
        ingreso1.setEstado(EstadoIngreso.EN_PROCESO);
        dbIngreso.guardar(ingreso1);

        String informeMedico = "";
        LocalDateTime fechaAtencion = LocalDateTime.now();

        try {
            moduloRegistroAtencion.registrarAtencion(ingreso1, medico, informeMedico, fechaAtencion);
            Assertions.fail("Debería haber lanzado una excepción porque el informe está vacío");

        } catch (Exception e) {
            Assertions.assertEquals(
                "El informe de la atención se ha omitido", 
                e.getMessage()
            );
        }
        Assertions.assertEquals(EstadoIngreso.EN_PROCESO, ingreso1.getEstado());

        Assertions.assertTrue(dbAtencion.obtenerAtenciones().isEmpty());
        Assertions.assertFalse(dbIngreso.obtenerFinalizados().contains(ingreso1)); 
        Assertions.assertFalse(dbIngreso.obtenerPendientes().contains(ingreso1));
    }
}
