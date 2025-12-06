package mock;

import org.example.app.interfaces.RepositorioMedicos;
import org.example.domain.Medico;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

public class DBMedicoEnMemoriaTest implements RepositorioMedicos {

    private Map<String, Medico> medicos;

    public DBMedicoEnMemoriaTest() {
        this.medicos = new HashMap<>();
    }

    @Override
    public void guardarMedico(Medico medico) {
        this.medicos.put(medico.getCuil(), medico);
    }

    @Override
    public Optional<Medico> buscarMedicoPorCuil(String cuil) {
        return Optional.ofNullable(medicos.get(cuil));
    }

    @Override
    public List<Medico> listarTodos() {
        return new ArrayList<>(medicos.values());
    }
}

