package org.example.infrastructure;

import org.example.app.interfaces.RepositorioAtenciones;
import org.example.domain.Atencion;
import org.example.domain.EstadoIngreso;
import org.example.domain.Medico;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RepositorioAtencionesEnMemoria implements RepositorioAtenciones{
    private List<Atencion> atenciones = new ArrayList<>();

    @Override
    public void guardar(Atencion atencion){
        atenciones.add(atencion);
    }

    @Override
    public List<Atencion> obtenerAtenciones(){
        return atenciones;
    }

    @Override
    public Optional<Atencion> obtenerPorMedico(Medico medico){
        return atenciones.stream()
                .filter(a -> a.getMedico().getCuil().equals(medico.getCuil()))
                .findFirst();
    }

    @Override
    public Optional<Atencion> obtenerAtencionActivaPorMedico(Medico medico){
        return atenciones.stream()
                .filter(a -> a.getMedico().getCuil().equals(medico.getCuil()))
                .filter(a -> a.getIngreso().getEstado() == EstadoIngreso.EN_PROCESO)
                .findFirst();
    }

    @Override
    public List<Atencion> obtenerFinalizadas(){
        return atenciones.stream()
                .filter(a -> a.getIngreso().getEstado() == EstadoIngreso.FINALIZADO)
                .sorted((a, b) -> b.getFechaAtencion().compareTo(a.getFechaAtencion()))
                .collect(Collectors.toList());
    }
}