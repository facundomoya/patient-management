package mock;

import java.util.ArrayList;
import java.util.List;
import org.example.domain.Atencion;

import org.example.app.interfaces.RepositorioAtenciones;

public class DBAtencionEnMemoria implements RepositorioAtenciones{
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
