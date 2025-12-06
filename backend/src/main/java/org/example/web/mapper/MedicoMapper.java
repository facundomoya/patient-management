package org.example.web.mapper;

import org.example.domain.Medico;
import org.example.web.dto.MedicoDTO;

public class MedicoMapper {
    public static MedicoDTO toDTO(Medico medico) {
        return new MedicoDTO(
            medico.getCuil(),
            medico.getNombre(),
            medico.getApellido()
        );
    }
}


