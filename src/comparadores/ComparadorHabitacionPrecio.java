package comparadores;

import java.util.Comparator;
import modelo.Habitacion;

public class ComparadorHabitacionPrecio implements Comparator<Habitacion> {

    @Override
    public int compare(Habitacion h1, Habitacion h2) {
        int cmpTarifa = Double.compare(h1.getTarifaPorNoche(), h2.getTarifaPorNoche());
        if (cmpTarifa != 0) {
            return cmpTarifa;
        }
        return Integer.compare(h1.getNumero(), h2.getNumero());
    }
}
