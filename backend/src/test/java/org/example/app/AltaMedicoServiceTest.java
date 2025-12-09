package org.example.app;

import mock.DBMedicoEnMemoriaTest;
import org.example.domain.Medico;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.*;

class AltaMedicoServiceTest {

    DBMedicoEnMemoriaTest repoMed;
    AltaMedicoService alta;

    @BeforeEach
    void setUp() {
        repoMed = new DBMedicoEnMemoriaTest();
        alta = new AltaMedicoService(repoMed);
    }

    @Test
    void crea_medico_con_datos_validos_ok() {
        String cuilEsperado = "20-39393175-3";  // CUIL válido
        String nombreEsperado = "Carlos";
        String apellidoEsperado = "Rodríguez";

        Medico m = alta.registrarMedico(
                cuilEsperado,
                nombreEsperado,
                apellidoEsperado
        );

        assertDatosMandatorios(m, cuilEsperado, nombreEsperado, apellidoEsperado);
    }

    @Test
    void medico_guardado_en_repositorio_ok() {
        String cuil = "20-39393175-3";
        Medico m = alta.registrarMedico(cuil, "Laura", "Fernández");

        var encontrado = repoMed.buscarMedicoPorCuil(cuil);
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getCuil()).isEqualTo(cuil);
    }

    @Test
    void listar_todos_los_medicos_ok() {
        alta.registrarMedico("20-39393175-3", "Medico1", "Apellido1");
        alta.registrarMedico("20-33538818-7", "Medico2", "Apellido2");

        var todos = repoMed.listarTodos();
        assertThat(todos).hasSize(2);
    }

    //assert auxiliar para validar datos mandatorios
    private void assertDatosMandatorios(Medico m, String cuil, String nombre, String apellido) {
        assertThat(m).isNotNull();
        assertThat(m.getCuil()).isEqualTo(cuil);
        assertThat(m.getNombre()).isEqualTo(nombre);
        assertThat(m.getApellido()).isEqualTo(apellido);
    }
}

