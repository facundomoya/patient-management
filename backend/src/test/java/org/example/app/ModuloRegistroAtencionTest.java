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

public class ModuloRegistroAtencionTest {
    private DBAtencionEnMemoria dbAtencion;
    private DBIngresoEnMemoria dbIngreso;
    private ServicioRegistroAtencion moduloRegistroAtencion;
    private Medico medico;
    private Enfermera enfermera;
    private Paciente p1;
    private Paciente p2;
    private Ingreso ingreso1;
    private Ingreso ingreso2;


    @BeforeEach
    void setUp(){
        dbAtencion = new DBAtencionEnMemoria();
        dbIngreso = new DBIngresoEnMemoria();
        moduloRegistroAtencion = new ServicioRegistroAtencion(dbAtencion, dbIngreso);

        medico = new Medico("Dr", "House", "20-43139260-8");
        enfermera = new Enfermera("Lucia", "Paz", "20-32456878-7");
        p1 = nuevoPaciente("20-40902338-0", "Facundo", "Moya");
        p2 = nuevoPaciente("20-43336577-2", "Santiago", "Martin");

        ingreso1 = new Ingreso(p1, enfermera, "Paciente prueba 1", NivelEmergencia.CRITICA, 37.0f, 80f, 18f, 120f, 80f);
        ingreso2 = new Ingreso(p2, enfermera, "Paciente prueba 2", NivelEmergencia.URGENCIA_MENOR, 37.0f, 80f, 18f, 120f, 80f);
    }

    private Paciente nuevoPaciente(String cuil, String nombre, String apellido) {
        // Domicilio mandatorio en tu dominio
        Domicilio dom = new Domicilio("San Martín", 1234, "Tucumán");
        Paciente p = new Paciente(cuil, nombre, apellido, dom);
        return p;
    }

    @Test
    void registrar_atencion_cambia_estado_a_finalizado_y_guarda() {
        // Simulamos un ingreso que ya estaba siendo atendido (EN_PROCESO)
        ingreso1.setEstado(EstadoIngreso.EN_PROCESO);
        dbIngreso.guardar(ingreso1);

        String informeMedico = "Paciente estabilizado y derivado a planta.";
        LocalDateTime fechaAtencion = LocalDateTime.now();

        Atencion resultadoAtencion = moduloRegistroAtencion.registrarAtencion(ingreso1, medico, informeMedico, fechaAtencion);

        
        // 1. Verificamos que el estado del ingreso cambió a FINALIZADO
        Assertions.assertEquals(EstadoIngreso.FINALIZADO, ingreso1.getEstado());

        // 2. Verificamos que la atención se guardó en el repositorio
        Assertions.assertEquals(resultadoAtencion, dbAtencion.obtenerAtenciones().contains(resultadoAtencion) ? resultadoAtencion : null);

        // 3. Verificamos que el ingreso ya no está en la lista de pendientes
        Assertions.assertFalse(dbIngreso.obtenerPendientes().contains(ingreso1));

        // 4. Esta es la prueba definitiva de que está en la lista maestra con el estado correcto:
        Assertions.assertTrue(dbIngreso.obtenerFinalizados().contains(ingreso1));
    }

    @Test
    void registrar_atencion_con_informe_omitido() {
        // Simulamos un ingreso que ya estaba siendo atendido (EN_PROCESO)
        ingreso1.setEstado(EstadoIngreso.EN_PROCESO);
        dbIngreso.guardar(ingreso1);

        String informeMedico = "";
        LocalDateTime fechaAtencion = LocalDateTime.now();

        try {
            moduloRegistroAtencion.registrarAtencion(ingreso1, medico, informeMedico, fechaAtencion);
            
            // Si el código llega a esta línea, significa que NO hubo error.
            // Entonces forzamos a que el test falle con un mensaje rojo.
            Assertions.fail("Debería haber lanzado una excepción porque el informe está vacío");

        } catch (Exception e) {
            // Verificamos que el mensaje sea el correcto
            Assertions.assertEquals(
                "El informe de la atención se ha omitido", 
                e.getMessage()
            );
        }

        // El sistema NO debió guardar nada ni cambiar el estado

        // El estado debe seguir siendo EN_PROCESO (no FINALIZADO)
        Assertions.assertEquals(EstadoIngreso.EN_PROCESO, ingreso1.getEstado());

        // La lista de atenciones debe seguir vacía
        Assertions.assertTrue(dbAtencion.obtenerAtenciones().isEmpty());
        
        // El ingreso no deberia estar en la lista de finalizados ni te pendientes
        Assertions.assertFalse(dbIngreso.obtenerFinalizados().contains(ingreso1)); 
        Assertions.assertFalse(dbIngreso.obtenerPendientes().contains(ingreso1));
    }
}
