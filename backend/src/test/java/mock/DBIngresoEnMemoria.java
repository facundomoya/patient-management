package mock;

import org.example.app.interfaces.RepositorioIngresos;
import org.example.domain.EstadoIngreso;
import org.example.domain.Ingreso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DBIngresoEnMemoria implements RepositorioIngresos {

    // Simula la tabla de base de datos "INGRESOS"
    // Usamos una lista porque el orden importa mucho en este caso
    private final List<Ingreso> tablaIngresos = new ArrayList<>();

    @Override
    public void guardar(Ingreso ingreso) {
        // Si ya existe (simulación de update), lo quitamos primero para no duplicar
        // Nota: Esto asume que Ingreso tiene un equals() basado en ID, si no, simplemente agrega.
        //tablaIngresos.remove(ingreso); 
        tablaIngresos.add(ingreso);
    }

    @Override
    public List<Ingreso> obtenerPendientes() {
        // Simula una consulta SQL: "SELECT * FROM ingresos WHERE estado = 'PENDIENTE' ORDER BY prioridad"
        return tablaIngresos.stream()
                .filter(ingreso -> ingreso.getEstado() == EstadoIngreso.PENDIENTE) // Solo los que esperan
                .sorted() // <--- AQUÍ ocurre la magia: Usa tu Ingreso::compareTo
                .collect(Collectors.toList());
    }

    @Override
    public void eliminarDePendientes(Ingreso ingreso) {
        // En realidad, en base de datos esto sería un UPDATE del estado,
        // pero para cumplir con la interfaz de "sacar de la lista":
        // Opción A: Lo borramos físicamente (si la interfaz dice eliminar)
        tablaIngresos.remove(ingreso);
        
        // Opción B (Más realista): Si tu lógica de negocio ya le cambió el estado a EN_PROCESO
        // antes de llamar a este método, el método 'obtenerPendientes' ya lo filtrará solo.
    }
}