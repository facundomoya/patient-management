// src/main/java/org/example/web/mapper/IngresoMapper.java
package org.example.web.mapper;

import org.example.domain.Ingreso;
import org.example.web.dto.IngresoDTO;

public class IngresoMapper {

    public static IngresoDTO toDTO(Ingreso ingreso) {
        IngresoDTO dto = new IngresoDTO();

        // ya tenés este metodo porque lo usás en los steps de Cucumber
        dto.setCuilPaciente(ingreso.getCuilPaciente());

        // acá usamos el paciente completo
        dto.setNombrePaciente(ingreso.getPaciente().getNombre());
        dto.setApellidoPaciente(ingreso.getPaciente().getApellido());

        dto.setNivelEmergencia(ingreso.getNivelEmergencia().name()); // o getNombre(), si tenés
        dto.setInforme(ingreso.getInforme());
        dto.setTemperatura(ingreso.getTemperatura());
        dto.setFrecuenciaCardiaca(ingreso.getFrecuenciaCardiaca());
        dto.setFrecuenciaRespiratoria(ingreso.getFrecuenciaRespiratoria());
        dto.setTensionSistolica(ingreso.getTensionSistolica());
        dto.setTensionDiastolica(ingreso.getTensionDiastolica());
        dto.setEstado(ingreso.getEstado().name());

        // Mapear CUIL de la enfermera (si existe)
        if (ingreso.getEnfermera() != null) {
            dto.setEnfermeroCuil(ingreso.getEnfermera().getCuil());
        }

        // Convertir LocalDateTime a String ISO para el frontend
        dto.setFechaIngreso(ingreso.getFechaIngreso().toString());

        return dto;
    }
}
