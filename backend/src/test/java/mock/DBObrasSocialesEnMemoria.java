package mock;

import org.example.app.interfaces.RepositorioObrasSociales;
import org.example.domain.ObraSocial;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

public class DBObrasSocialesEnMemoria implements RepositorioObrasSociales {

    private final Map<String, ObraSocial> data = new HashMap<>();

    @Override
    public void guardar(ObraSocial os) {
        data.put(os.getCodigo(), os);
    }

    @Override
    public Optional<ObraSocial> buscarPorCodigo(String codigo) {
        return Optional.ofNullable(data.get(codigo));
    }

    @Override
    public List<ObraSocial> listarTodas() {
        // implementación simple para tests
        return new ArrayList<>(data.values());
    }
}
