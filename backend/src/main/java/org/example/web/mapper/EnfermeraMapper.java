package org.example.web.mapper;

import org.example.domain.Enfermera;
import org.example.web.dto.EnfermeraDTO;

public class EnfermeraMapper {
    public static EnfermeraDTO toDTO(Enfermera enfermera) {
        return new EnfermeraDTO(
            enfermera.getCuil(),
            enfermera.getNombre(),
            enfermera.getApellido()
        );
    }
}


