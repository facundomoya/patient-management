package org.example.app;

import mock.DBEnfermeraEnMemoriaTest;
import org.example.domain.Enfermera;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class AltaEnfermeraServiceTest {

    DBEnfermeraEnMemoriaTest repoEnf;
    AltaEnfermeraService alta;

    @BeforeEach
    void setUp() {
        repoEnf = new DBEnfermeraEnMemoriaTest();
        alta = new AltaEnfermeraService(repoEnf);
    }

    @Test
    void crea_enfermera_con_datos_validos_ok() {
        String cuilEsperado = "27-12345678-9";
        String nombreEsperado = "María";
        String apellidoEsperado = "González";

        Enfermera e = alta.registrarEnfermera(
                cuilEsperado,
                nombreEsperado,
                apellidoEsperado
        );

        assertDatosMandatorios(e, cuilEsperado, nombreEsperado, apellidoEsperado);
    }

    @Test
    void enfermera_guardada_en_repositorio_ok() {
        String cuil = "27-23456789-0";
        Enfermera e = alta.registrarEnfermera(cuil, "Ana", "Martínez");

        Optional<Enfermera> encontrada = repoEnf.buscarEnfermeraPorCuil(cuil);
        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().getCuil()).isEqualTo(cuil);
    }

    @Test
    void listar_todas_las_enfermeras_ok() {
        alta.registrarEnfermera("27-11111111-1", "Enfermera1", "Apellido1");
        alta.registrarEnfermera("27-22222222-2", "Enfermera2", "Apellido2");

        var todas = repoEnf.listarTodas();
        assertThat(todas).hasSize(2);
    }

    //assert auxiliar para validar datos mandatorios
    private void assertDatosMandatorios(Enfermera e, String cuil, String nombre, String apellido) {
        assertThat(e).isNotNull();
        assertThat(e.getCuil()).isEqualTo(cuil);
        assertThat(e.getNombre()).isEqualTo(nombre);
        assertThat(e.getApellido()).isEqualTo(apellido);
    }
}

