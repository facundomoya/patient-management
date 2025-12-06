package mock;

import org.example.app.interfaces.RepositorioMedicos;
import org.example.domain.Medico;

import java.util.*;

public class DBMedicoEnMemoria implements RepositorioMedicos {
    private final Map<String, Medico> porCuil = new HashMap<>();

    public void guardar(Medico m) { 
        porCuil.put(m.getCuil(), m); 
    }
    
    @Override
    public void guardarMedico(Medico medico) {
        porCuil.put(medico.getCuil(), medico);
    }

    @Override 
    public Optional<Medico> buscarMedicoPorCuil(String cuil) {
        return Optional.ofNullable(porCuil.get(cuil));
    }
    
    @Override
    public List<Medico> listarTodos() {
        return new ArrayList<>(porCuil.values());
    }
}