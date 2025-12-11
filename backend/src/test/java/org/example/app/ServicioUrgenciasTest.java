package org.example.app;

import mock.DBIngresoEnMemoria;
import mock.DBPacienteEnMemoria;
import org.example.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ServicioUrgenciasTest {

    private DBPacienteEnMemoria db;
    private DBIngresoEnMemoria dbI;
    private ServicioUrgencias servicio;
    private Enfermera enfermera;

    @BeforeEach
    void setUp() {
        db = new DBPacienteEnMemoria();
        dbI = new DBIngresoEnMemoria();
        servicio = new ServicioUrgencias(db, dbI);
        enfermera = new Enfermera("Lucia", "Paz", "20-32456878-7");
    }

    private Paciente nuevoPaciente(String cuil, String nombre, String apellido) {
        Domicilio dom = new Domicilio("San Martín", 1234, "Tucumán");
        Paciente p = new Paciente(cuil, nombre, apellido, dom);
        db.guardarPaciente(p);
        return p;
    }

    @Test
    void ordena_por_nivel_de_prioridad_y_luego_por_llegada() {
        Paciente p1 = nuevoPaciente("20-40902338-0", "Facundo", "Moya");
        servicio.registrarUrgencia(
                p1.getCuil(), enfermera, "dolor", NivelEmergencia.URGENCIA,
                37.0f, 80f, 18f, 120f, 80f
        );
        Paciente p2 = nuevoPaciente("20-43336577-2", "Santiago", "Martin");
        servicio.registrarUrgencia(
                p2.getCuil(), enfermera, "critico", NivelEmergencia.CRITICA,
                37.5f, 90f, 20f, 130f, 85f
        );

        var orden = servicio.obtenerIngresosPendientes()
                .stream().map(Ingreso::getCuilPaciente).toList();

        assertThat(orden).containsExactly(p2.getCuil(), p1.getCuil());
    }

    @Test
    void mismo_nivel_prioridad_respeta_orden_de_llegada() throws InterruptedException {
        Paciente p1 = nuevoPaciente("20-40902338-0", "Facundo", "Moya");
        servicio.registrarUrgencia(
                p1.getCuil(), enfermera, "dolor leve", NivelEmergencia.URGENCIA_MENOR,
                36.9f, 75f, 16f, 118f, 78f
        );
        Thread.sleep(5);

        Paciente p2 = nuevoPaciente("20-43336577-2", "Santiago", "Martin");
        servicio.registrarUrgencia(
                p2.getCuil(), enfermera, "control", NivelEmergencia.URGENCIA_MENOR,
                37.1f, 82f, 19f, 122f, 81f
        );

        var orden = servicio.obtenerIngresosPendientes()
                .stream().map(Ingreso::getCuilPaciente).toList();
        assertThat(orden).containsExactly(p1.getCuil(), p2.getCuil());
    }
}
