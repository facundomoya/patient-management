package org.example.app;

import mock.DBObrasSocialesEnMemoria;
import mock.DBPacienteEnMemoria;
import org.example.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AltaPacienteServiceTest {

    DBPacienteEnMemoria repoPac;
    DBObrasSocialesEnMemoria repoOS;
    ServicioAltaPaciente alta;

    @BeforeEach
    void setUp() {
        repoPac = new DBPacienteEnMemoria();
        repoOS  = new DBObrasSocialesEnMemoria();
        // catálogo con OSDE
        repoOS.guardar(new ObraSocial("OSDE", "OSDE"));
        alta = new ServicioAltaPaciente(repoPac, repoOS);
    }

    @Test
    void crea_con_mandatorios_sin_obra_socialcon_codigo_null_ok() {
        String cuilEsperado = "20-40902338-0";
        String nombreEsperado = "Facundo";
        String apellidoEsperado = "Moya";

        Paciente p = alta.registrarPaciente(
                cuilEsperado, nombreEsperado, apellidoEsperado,
                "San Martín", 1234, "Tucumán",
                null, null
        );
        assertThat(p.tieneAfiliacion()).isFalse();

        assertDatosMandatorios(p,cuilEsperado, nombreEsperado, apellidoEsperado);
    }

    @Test
    void crea_con_mandatorios_sin_obra_social_con_codigo_vacio_ok() {
        String cuilEsperado = "20-40902338-0";
        String nombreEsperado = "Facundo";
        String apellidoEsperado = "Moya";

        Paciente p = alta.registrarPaciente(
                cuilEsperado, nombreEsperado, apellidoEsperado,
                "San Martín", 1234, "Tucumán",
                "", null
        );
        assertThat(p.tieneAfiliacion()).isFalse();

        assertDatosMandatorios(p, cuilEsperado, nombreEsperado, apellidoEsperado);
    }

    @Test
    void crea_con_mandatorios_sin_obra_social_con_codigo_con_espacio_ok() {
        String cuilEsperado = "20-40902338-0";
        String nombreEsperado = "Facundo";
        String apellidoEsperado = "Moya";

        Paciente p = alta.registrarPaciente(
                cuilEsperado, nombreEsperado, apellidoEsperado,
                "San Martín", 1234, "Tucumán",
                "   ", null
        );
        assertThat(p.tieneAfiliacion()).isFalse();

        assertDatosMandatorios(p, cuilEsperado, nombreEsperado, apellidoEsperado);
    }

    @Test
    void crea_con_mandatorios_y_obra_social_existente_ok() {
        String cuilEsperado = "20-43336577-2";
        String nombreEsperado = "Santiago";
        String apellidoEsperado = "Martín";

        Paciente p = alta.registrarPaciente(
                cuilEsperado, nombreEsperado, apellidoEsperado,
                "Laprida", 321, "Tucumán",
                "OSDE", "45-889900"
        );
        assertThat(p.tieneAfiliacion()).isTrue();
        assertThat(p.getAfiliacion().getObraSocial().getCodigo()).isEqualTo("OSDE");

        assertDatosMandatorios(p, cuilEsperado, nombreEsperado, apellidoEsperado);
    }

    @Test
    void crea_con_obra_social_afiliado_con_espacios_ok() {
        String cuilEsperado = "20-43336577-2";
        String nombreEsperado = "Santiago";
        String apellidoEsperado = "Martín";

        Paciente p = alta.registrarPaciente(
                cuilEsperado, nombreEsperado, apellidoEsperado,
                "Laprida", 321, "Tucumán",
                "OSDE", " 45-889900 " // Número con espacios en blanco
        );
        // Asumiendo que Afiliacion hace trim() al número antes de guardarlo.
        assertThat(p.getAfiliacion().getNumeroAfiliado()).isEqualTo("45-889900");

        assertDatosMandatorios(p, cuilEsperado, nombreEsperado, apellidoEsperado);
    }

    @Test
    void obra_social_inexistente_lanza_error_pedido() {
        assertThatThrownBy(() -> alta.registrarPaciente(
                "20-43336577-2", "Santiago", "Martin",
                "Laprida", 321, "Tucumán",
                "OSECAC", "12345"
        )).isInstanceOf(org.example.domain.Exceptions.DomainException.class)
                .hasMessageContaining("obra social inexistente");
    }

    @Test
    void obra_social_existente_sin_numero_afiliado_lanza_error_pedido() {
        assertThatThrownBy(() -> alta.registrarPaciente(
                "20-43336577-2", "Santiago", "Martin",
                "Laprida", 321, "Tucumán",
                "OSDE", "   "
        )).isInstanceOf(org.example.domain.Exceptions.DomainException.class)
                .hasMessageContaining("no está afiliado");
    }

    @Test
    void dato_mandatorio_faltante_calle_en_domicilio_lanza() {
        assertThatThrownBy(() -> alta.registrarPaciente(
                "20-43336577-2", "Santiago", "Martin",
                "   ", 321, "Tucumán",
                null, null
        )).isInstanceOf(org.example.domain.Exceptions.DomainException.class)
                .hasMessageContaining("calle");
    }

    @Test
    void dato_mandatorio_faltante_numero_en_domicilio_lanza() {
        assertThatThrownBy(() -> alta.registrarPaciente(
                "20-43336577-2", "Santiago", "Martin",
                "Laprida", null, "Tucumán",
                null, null
        )).isInstanceOf(org.example.domain.Exceptions.DomainException.class)
                .hasMessageContaining("número");
    }

    @Test
    void testDato_mandatorio_faltante_localidad_en_domicilio_lanza() {
        // Escenario: La Calle y Localidad son válidas, pero el Número de domicilio es NULL.
        assertThatThrownBy(() -> alta.registrarPaciente(
                "20-43336577-2", "Santiago", "Martin",
                "Laprida", 123, null,
                null, null
        )).isInstanceOf(org.example.domain.Exceptions.DomainException.class)
                // Se asume que la excepción menciona el campo 'número' o 'domicilio'
                .hasMessageContaining("localidad");
    }




    //assert auxiliar para validar datos mandatorios en cada test y hacer mas legible el codigo
    private void assertDatosMandatorios(Paciente p, String cuil, String nombre, String apellido) {
        // Verifica que el paciente fue guardado
        assertThat(p).isNotNull();

        // Verifica los atributos directos
        assertThat(p.getCuil()).isEqualTo(cuil);
        assertThat(p.getNombre()).isEqualTo(nombre);
        assertThat(p.getApellido()).isEqualTo(apellido);
    }
}


