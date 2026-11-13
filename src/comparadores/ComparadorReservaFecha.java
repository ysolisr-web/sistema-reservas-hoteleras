package comparadores;

import java.util.Comparator;
import modelo.Reserva;

public class ComparadorReservaFecha implements Comparator<Reserva> {

    @Override
    public int compare(Reserva r1, Reserva r2) {
        int cmpFecha = r1.getFecha().compareTo(r2.getFecha());
        if (cmpFecha != 0) {
            return cmpFecha;
        }
        return r1.getCodigo().compareToIgnoreCase(r2.getCodigo());
    }
}
