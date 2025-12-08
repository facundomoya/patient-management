package org.example.domain;

import org.example.domain.Exceptions.DomainException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PersonaTest {
    @Test
    void crea_persona_valida_normaliza_cuil() {
        Persona p = new Persona("20-40902338-0", " Facundo ", " Moya ");
        assertThat(p.getCuil()).isEqualTo("20-40902338-0");
        assertThat(p.getNombre()).isEqualTo("Facundo");
        assertThat(p.getApellido()).isEqualTo("Moya");
    }

    @Test
    void nombre_blanco_lanza() {
        assertThatThrownBy(() -> new Persona("20-40902338-0", "   ", "Moya"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Nombre");
    }

    @Test
    void apellido_blanco_lanza() {
        assertThatThrownBy(() -> new Persona("20-40902338-0", "Facundo", "   "))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Apellido");
    }

    @Test
    void cuil_invalido_lanza() {
        assertThatThrownBy(() -> new Persona("20-40902338-7", "Facundo", "Moya"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("CUIL");
    }
}