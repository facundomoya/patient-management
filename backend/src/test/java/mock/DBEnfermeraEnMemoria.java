package mock;

import org.example.app.interfaces.RepositorioEnfermeras;
import org.example.domain.Enfermera;

import java.util.*;

public class DBEnfermeraEnMemoria implements RepositorioEnfermeras {
    private final Map<String, Enfermera> porCuil = new HashMap<>();
    
    public void guardar(Enfermera e) { 
        porCuil.put(e.getCuil(), e); 
    }
    
    @Override 
    public void guardarEnfermera(Enfermera enfermera) {
        porCuil.put(enfermera.getCuil(), enfermera);
    }
    
    @Override 
    public Optional<Enfermera> buscarEnfermeraPorCuil(String cuil) {
        return Optional.ofNullable(porCuil.get(cuil));
    }
    
    @Override
    public List<Enfermera> listarTodas() {
        return new ArrayList<>(porCuil.values());
    }
}