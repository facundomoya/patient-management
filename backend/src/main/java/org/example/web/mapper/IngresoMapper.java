// src/main/java/org/example/web/mapper/IngresoMapper.java
package org.example.web.mapper;

import org.example.domain.Ingreso;
import org.example.web.dto.IngresoDTO;

public class IngresoMapper {

    public static IngresoDTO toDTO(Ingreso ingreso) {
        IngresoDTO dto = new IngresoDTO();

        dto.setCuilPaciente(ingreso.getCuilPaciente());

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

        if (ingreso.getEnfermera() != null) {
            dto.setEnfermeroCuil(ingreso.getEnfermera().getCuil());
        }

        dto.setFechaIngreso(ingreso.getFechaIngreso().toString());

        return dto;
    }
}
