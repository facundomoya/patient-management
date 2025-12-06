package org.example.app;

import org.example.domain.Domicilio;
import org.example.domain.Enfermera;
import org.example.domain.EstadoIngreso;
import org.example.domain.Ingreso;
import org.example.domain.NivelEmergencia;
import org.example.domain.Paciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import mock.DBIngresoEnMemoria;

public class ModuloReclamoTest {
    private DBIngresoEnMemoria dbIng;
    private ModuloReclamo moduloRecl;
    private Enfermera enfermera;
    private Paciente p1;
    private Paciente p2;


    @BeforeEach
    void setUp(){
        dbIng = new DBIngresoEnMemoria();
        moduloRecl = new ModuloReclamo(dbIng);

        enfermera = new Enfermera("Lucia", "Paz", "20-32456878-7");

        p1 = nuevoPaciente("20-40902338-0", "Facundo", "Moya");
        p2 = nuevoPaciente("20-43336577-2", "Santiago", "Martin");
        }

    private Paciente nuevoPaciente(String cuil, String nombre, String apellido) {
        // Domicilio mandatorio en tu dominio
        Domicilio dom = new Domicilio("San Martín", 1234, "Tucumán");
        Paciente p = new Paciente(cuil, nombre, apellido, dom);
        return p;
    }

    @Test
    public void medico_debe_atender_al_paciente_con_mayor_prioridad(){
        //Creo los ingresos para los 2 pacientes
        Ingreso ingreso1 = new Ingreso(p1, enfermera, "Paciente prueba 1", NivelEmergencia.URGENCIA_MENOR, 37.0f, 80f, 18f, 120f, 80f);
        Ingreso ingreso2 = new Ingreso(p2, enfermera, "Paciente prueba 2", NivelEmergencia.CRITICA, 37.0f, 80f, 18f, 120f, 80f);

        //Los guardo en la memoria mockeada, desordenados para que de paso los ordene
        dbIng.guardar(ingreso1);
        dbIng.guardar(ingreso2);

        //Pido que me devuelva el paciente que sacaria el medico de la lista, deberia ser el primero, el de mas alta prioridad
        Ingreso resultado = moduloRecl.reclamarSiguientePaciente();

        Assertions.assertEquals(NivelEmergencia.CRITICA, resultado.getNivelEmergencia());
        
        Assertions.assertEquals(EstadoIngreso.EN_PROCESO, resultado.getEstado());
        
        Assertions.assertEquals(p1, dbIng.obtenerPendientes().get(0).getPaciente());

        Assertions.assertFalse(dbIng.obtenerPendientes().contains(resultado), "El paciente atendido no debería seguir en la lista de espera");
    }


    @Test
    public void medico_reclama_pero_no_hay_nadie_en_la_lista(){
        try {
            moduloRecl.reclamarSiguientePaciente();
            // Si el código llega a esta línea, significa que NO lanzó error.
            // Por lo tanto, forzamos a que el test falle.
            org.junit.jupiter.api.Assertions.fail("Debería haber lanzado una excepción");
            
        } catch (RuntimeException e) {
            // Verificamos que el mensaje sea el correcto
            Assertions.assertEquals(
                "No hay pacientes en espera", 
                e.getMessage()
            );
        }
    }


}
