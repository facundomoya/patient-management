package org.example.web.mapper;

import org.example.domain.Atencion;
import org.example.web.dto.AtencionDTO;

public class AtencionMapper {
    public static AtencionDTO toDTO(Atencion atencion) {
        AtencionDTO dto = new AtencionDTO();
        
        dto.setCuilPaciente(atencion.getIngreso().getCuilPaciente());
        dto.setNombrePaciente(atencion.getIngreso().getPaciente().getNombre());
        dto.setApellidoPaciente(atencion.getIngreso().getPaciente().getApellido());
        
        dto.setCuilMedico(atencion.getMedico().getCuil());
        dto.setNombreMedico(atencion.getMedico().getNombre());
        dto.setApellidoMedico(atencion.getMedico().getApellido());
        
        dto.setInforme(atencion.getInforme());
        dto.setFechaAtencion(atencion.getFechaAtencion());

        dto.setNivelEmergencia(atencion.getIngreso().getNivelEmergencia().name());
        
        return dto;
    }
}

