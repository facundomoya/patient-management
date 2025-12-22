package org.example.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import mock.DBIngresoEnMemoria;
import mock.DBPacienteEnMemoria;
import org.example.app.ServicioUrgencias;
import org.example.domain.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;
import static org.assertj.core.api.Assertions.assertThat;


public class ModuloUrgenciasNuevoStepDefinitions {

    private final DBPacienteEnMemoria dbMockeada;
    private final DBIngresoEnMemoria dbIngMockeada;
    private final ServicioUrgencias servicioUrgencias;

    private Enfermera enfermera;       // Objeto enfermera usado por el servicio
    private String enfermeraCuil;      // CUIL de la enfermera tal como viene en el Background
    private Exception excepcionEsperada;

    public ModuloUrgenciasNuevoStepDefinitions() {
        this.dbMockeada = new DBPacienteEnMemoria();
        this.dbIngMockeada = new DBIngresoEnMemoria();
        this.servicioUrgencias = new ServicioUrgencias(dbMockeada, dbIngMockeada);
    }

    // Background: enfermera con columnas: CUIL | Nombre | Apellido
    @Given("que la siguiente enfermera esta registrada:")
    public void queLaSiguienteEnfermeraEstaRegistradaConCuil(List<Map<String, String>> tabla) {
        Map<String, String> fila = tabla.get(0);
        this.enfermeraCuil = get(fila, "Cuil");
        String nombre = get(fila, "Nombre");
        String apellido = get(fila, "Apellido");
        // Si tu clase Enfermera solo admite (nombre, apellido), conservamos ese constructor.
        this.enfermera = new Enfermera(nombre, apellido, enfermeraCuil);
    }

    // Background: pacientes con columnas: CUIL | Nombre | Apellido | Obra social
    @Given("que los siguientes pacientes esten registrados:")
    public void queLosSiguientesPacientesEstenRegistrados(List<Map<String, String>> tabla) {
        for (Map<String, String> fila : tabla) {
            String cuil      = get(fila, "Cuil");
            String nombre    = get(fila, "Nombre");
            String apellido  = get(fila, "Apellido");

            // Domicilio MANDATORIO en dominio
            String calle     = get(fila, "Calle");
            Integer numero   = parseInt(get(fila, "Numero"));
            String localidad = get(fila, "Localidad");
            Domicilio domicilio = new Domicilio(calle, numero, localidad);

            // Afiliación OPCIONAL
            String osCod     = get(fila, "Obra Social Codigo");
            String osNom     = get(fila, "Obra Social Nombre");
            String nroAfi    = get(fila, "Numero Afiliado");

            Paciente p;
            if (!isBlank(osCod) && !isBlank(osNom)) {
                ObraSocial os = new ObraSocial(osCod, osNom);
                Afiliacion afi = new Afiliacion(os, nroAfi);
                p = new Paciente(cuil, nombre, apellido, domicilio, afi);
            } else {
                p = new Paciente(cuil, nombre, apellido, domicilio);
            }

            dbMockeada.guardarPaciente(p);
        }
    }

    @When("ingresa el siguiente paciente:")
    public void ingresaElSiguientePaciente(List<Map<String, String>> tabla) {
        excepcionEsperada = null;
        Map<String, String> fila = tabla.get(0);

        // ===== Mandatorios (strings) =====
        String cuilPaciente = get(fila, "Cuil");
        if (isBlank(cuilPaciente)) {
            excepcionEsperada = new IllegalArgumentException("El CUIL del paciente es obligatorio");
            return;
        }

        String informe = get(fila, "Informe");
        if (isBlank(informe)) {
            excepcionEsperada = new IllegalArgumentException("El informe es obligatorio");
            return;
        }

        String nivelStr = get(fila, "Nivel de emergencia");
        if (isBlank(nivelStr)) {
            excepcionEsperada = new IllegalArgumentException("El nivel de emergencia es obligatorio");
            return;
        }


        String frecCardStr = get(fila, "Frecuencia cardiaca");
        //  String frecCardStr       = "80";
        if (isBlank(frecCardStr)) {
            excepcionEsperada = new IllegalArgumentException("La frecuencia cardiaca es obligatoria");
            return;
        }

        String frecRespStr = get(fila, "Frecuencia respiratoria");
        if (isBlank(frecRespStr)) {
            excepcionEsperada = new IllegalArgumentException("La frecuencia respiratoria es obligatoria");
            return;
        }

        String tensionSisStr = get(fila, "Tension sistolica");
        if (isBlank(tensionSisStr)) {
            excepcionEsperada = new IllegalArgumentException("La tensión sistolica es obligatoria");
            return;
        }

        String tensionDiaStr = get(fila, "Tension diastolica");
        if (isBlank(tensionDiaStr)) {
            excepcionEsperada = new IllegalArgumentException("La tensión diastolica es obligatoria");
            return;
        }

        String cuilEnfermeraFila = get(fila, "CUIL enfermera");
        if (isBlank(cuilEnfermeraFila)) {
            excepcionEsperada = new IllegalArgumentException("El CUIL de la enfermera es obligatorio");
            return;
        }

        // ===== Parsing (Temperatura puede ser null) =====
        String temperaturaStr = get(fila, "Temperatura"); // opcional
        Float temperatura = isBlank(temperaturaStr) ? null : parseFloat(temperaturaStr);

        Float frecCard = parseFloat(frecCardStr);
        Float frecResp = parseFloat(frecRespStr);
        //Float frecResp = parseFloat("20");
        Float tensionSis = parseFloat(tensionSisStr);
        Float tensionDia = parseFloat(tensionDiaStr);

        // ===== Validaciones de valores negativos =====
        if (frecCard < 0)   { excepcionEsperada = new IllegalArgumentException("La frecuencia cardiaca no puede ser negativa"); return; }
        if (frecResp < 0)   { excepcionEsperada = new IllegalArgumentException("La frecuencia respiratoria no puede ser negativa"); return; }
        if (tensionSis < 0) { excepcionEsperada = new IllegalArgumentException("La tensión sistolica no puede ser negativa"); return; }
        if (tensionDia < 0) { excepcionEsperada = new IllegalArgumentException("La tensión diastolica no puede ser negativa"); return; }

        // Nivel
        NivelEmergencia nivel = Arrays.stream(NivelEmergencia.values())
                .filter(ne -> ne.tieneNombre(nivelStr))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Nivel desconocido: " + nivelStr));

        // Validación de consistencia con el Background
        if (enfermeraCuil != null && !enfermeraCuil.equals(cuilEnfermeraFila)) {
            excepcionEsperada = new Exception("El CUIL de la enfermera (" + cuilEnfermeraFila +
                    ") no coincide con el del Background (" + enfermeraCuil + ")");
            return;
        }

        try {
            servicioUrgencias.registrarUrgencia(
                    cuilPaciente,
                    enfermera,
                    informe,
                    nivel,
                    temperatura,
                    frecCard,
                    frecResp,
                    tensionSis,
                    tensionDia
            );
        } catch (Exception e) {
            this.excepcionEsperada = e;
        }
    }

    // ===== Helpers =====
    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }


    // Then con tabla: CUIL | Nombre | Apellido (usamos solo CUIL para la verificación)
    @Then("la lista de espera esta ordenada segun el nivel de emergencia por cuil de la siguiente manera:")
    public void laListaOrdenadaSegunNivelPorCuil(List<Map<String, String>> tabla) {
        List<String> cuilsEsperados = tabla.stream()
                .map(f -> get(f, "Cuil"))
                .toList();

        List<String> cuilsPendientes = servicioUrgencias.obtenerIngresosPendientes()
                .stream()
                .map(Ingreso::getCuilPaciente)
                .toList();

        assertThat(cuilsPendientes)
                .hasSize(cuilsEsperados.size())
                .containsExactlyElementsOf(cuilsEsperados);
    }


    // ===== Helpers =====

    private static String get(Map<String, String> fila, String key) {
        // Acceso seguro: trim a la clave y al valor para tolerar espacios incidentales en tablas
        String val = fila.getOrDefault(key, fila.getOrDefault(key.trim(), null));
        return val == null ? null : val.trim();
    }

    private static Float parseFloat(String s) {
        if (s == null) return null;
        return Float.parseFloat(s.trim());
    }


    @Then("el sistema muestra un error indicando la {string}")
    public void elSistemaMuestraUnErrorIndicandoLa(String mensajeEsperado) {
        assertThat(excepcionEsperada)
                .as("Se esperaba un error")
                .isNotNull();

        assertThat(excepcionEsperada.getMessage())
                .as("El mensaje real no contiene el esperado")
                .isNotNull()
                .containsIgnoringCase(mensajeEsperado); // sirve para “es obligatoria” y “no puede ser negativa”
    }

    @Given("la lista de espera actual es:")
    public void laListaDeEsperaActualEs(List<Map<String, String>> tabla) {
        if (enfermera == null) {
            throw new AssertionError("No hay enfermera inicializada desde el Background.");
        }

        for (Map<String, String> fila : tabla) {
            String cuil     = get(fila, "Cuil");
            String nombre   = get(fila, "Nombre");
            String apellido = get(fila, "Apellido");
            String nivelStr = get(fila, "Nivel de emergencia");

            NivelEmergencia nivel = Arrays.stream(NivelEmergencia.values())
                    .filter(ne -> ne.tieneNombre(nivelStr))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Nivel desconocido: " + nivelStr));

            try {
                servicioUrgencias.registrarUrgencia(
                        cuil, enfermera, "Precargado en lista de espera",
                        nivel, 36.8f, 72f, 16f, 120f, 80f
                );
            } catch (Exception e) {
                throw new AssertionError(
                        "No se pudo precargar el paciente " + cuil +
                                " (" + nombre + " " + apellido + "). ", e
                );
            }
        }
    }


    @Then("el sistema prioriza al paciente por su nivel de emergencia:")
    public void elSistemaPriorizaAlPacientePorSuNivelDeEmergencia(List<Map<String, String>> tabla) {
        List<String> cuilsEsperados = tabla.stream()
                .map(f -> get(f, "Cuil"))
                .toList();

        List<String> cuilsPendientes = servicioUrgencias.obtenerIngresosPendientes()
                .stream()
                .map(Ingreso::getCuilPaciente)
                .toList();

        assertThat(cuilsPendientes)
                .as("El orden de prioridad en la lista de espera no coincide con lo esperado")
                .hasSize(cuilsEsperados.size())
                .containsExactlyElementsOf(cuilsEsperados);
    }

}
