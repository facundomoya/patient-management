package org.example.infrastructure;

import org.example.app.interfaces.RepositorioAtenciones;
import org.example.domain.Atencion;
import java.util.ArrayList;
import java.util.List;

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
}
