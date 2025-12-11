package org.example.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.example.app.interfaces.RepositorioIngresos;
import org.example.domain.Ingreso;
import org.example.domain.EstadoIngreso;

public class RepositorioIngresosEnMemoria implements RepositorioIngresos{

    private final List<Ingreso> listaEspera = new ArrayList<>();

    @Override
    public void guardar(Ingreso ingreso){
        listaEspera.add(ingreso);

        listaEspera.sort(Ingreso::compareTo);
        //listaEspera.sort((i1, i2) -> i1.compareTo(i2));
    }

    @Override
    public List<Ingreso> obtenerPendientes(){
        return listaEspera.stream().filter(i -> i.getEstado() == EstadoIngreso.PENDIENTE)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ingreso> obtenerTodos() {
        return new ArrayList<>(listaEspera);
    }

    @Override
    public void eliminarDePendientes(Ingreso ingreso){
        listaEspera.remove(ingreso);
    }

    @Override
    public List<Ingreso> obtenerFinalizados(){
        return listaEspera.stream().filter(i -> i.getEstado() == EstadoIngreso.FINALIZADO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ingreso> obtenerEnProceso(){
        return listaEspera.stream().filter(i -> i.getEstado() == EstadoIngreso.EN_PROCESO)
                .collect(Collectors.toList());
    }

}
