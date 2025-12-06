package mock;

import org.example.app.interfaces.RepositorioEnfermeras;
import org.example.domain.Enfermera;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

public class DBEnfermeraEnMemoriaTest implements RepositorioEnfermeras {

    private Map<String, Enfermera> enfermeras;

    public DBEnfermeraEnMemoriaTest() {
        this.enfermeras = new HashMap<>();
    }

    @Override
    public void guardarEnfermera(Enfermera enfermera) {
        this.enfermeras.put(enfermera.getCuil(), enfermera);
    }

    @Override
    public Optional<Enfermera> buscarEnfermeraPorCuil(String cuil) {
        return Optional.ofNullable(enfermeras.get(cuil));
    }

    @Override
    public List<Enfermera> listarTodas() {
        return new ArrayList<>(enfermeras.values());
    }
}

