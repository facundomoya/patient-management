package mock;

import org.example.app.interfaces.RepositorioIngresos;
import org.example.domain.EstadoIngreso;
import org.example.domain.Ingreso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DBIngresoEnMemoria implements RepositorioIngresos {
    private final List<Ingreso> tablaIngresos = new ArrayList<>();

    @Override
    public void guardar(Ingreso ingreso) {
        tablaIngresos.add(ingreso);
    }

    @Override
    public List<Ingreso> obtenerPendientes() {
        return tablaIngresos.stream()
                .filter(ingreso -> ingreso.getEstado() == EstadoIngreso.PENDIENTE)
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public List<Ingreso> obtenerFinalizados() {
        return tablaIngresos.stream()
                .filter(ingreso -> ingreso.getEstado() == EstadoIngreso.FINALIZADO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ingreso> obtenerTodos() {
        return new ArrayList<>(tablaIngresos);
    }

    @Override
    public List<Ingreso> obtenerEnProceso() {
        return tablaIngresos.stream()
                .filter(ingreso -> ingreso.getEstado() == EstadoIngreso.EN_PROCESO)
                .collect(Collectors.toList());
    }

    @Override
    public void eliminarDePendientes(Ingreso ingreso) {
        tablaIngresos.remove(ingreso);
    }
}
