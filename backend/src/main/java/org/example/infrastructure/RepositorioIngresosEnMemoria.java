package org.example.infrastructure;

import java.util.ArrayList;
import java.util.List;

import org.example.app.interfaces.RepositorioIngresos;
import org.example.domain.Ingreso;

public class RepositorioIngresosEnMemoria implements RepositorioIngresos{

    private final List<Ingreso> listaEspera = new ArrayList<>();

    @Override
    public void guardar(Ingreso ingreso){
        listaEspera.add(ingreso);

        listaEspera.sort(Ingreso::compareTo);
    }

    @Override
    public List<Ingreso> obtenerPendientes(){
        return listaEspera;
    }

    @Override
    public void eliminarDePendientes(Ingreso ingreso){
        listaEspera.remove(ingreso);
    }

}
