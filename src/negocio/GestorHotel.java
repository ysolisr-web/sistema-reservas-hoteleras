package negocio;

import comparadores.ComparadorHabitacionPrecio;
import comparadores.ComparadorReservaFecha;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;
import modelo.Habitacion;
import modelo.Huesped;
import modelo.Reserva;

public class GestorHotel {

    private final Map<String, Huesped> huespedes;
    private final Set<Habitacion> catalogoHabitaciones;
    private final TreeSet<Habitacion> habitacionesDisponibles;
    private final List<Reserva> historialReservas;
    private final PriorityQueue<Reserva> colaAtencion;

    public GestorHotel() {
        this.huespedes = new HashMap<>();
        this.catalogoHabitaciones = new HashSet<>();
        this.habitacionesDisponibles = new TreeSet<>(new ComparadorHabitacionPrecio());
        this.historialReservas = new ArrayList<>();
        this.colaAtencion = new PriorityQueue<>(new ComparadorReservaFecha());
        cargarDatosIniciales();
    }

    private void cargarDatosIniciales() {
        registrarHabitacion(new Habitacion(101, "Sencilla", 120000, true));
        registrarHabitacion(new Habitacion(102, "Doble", 180000, true));
        registrarHabitacion(new Habitacion(201, "Suite", 320000, true));
        registrarHabitacion(new Habitacion(202, "Doble", 175000, true));

        registrarHuesped(new Huesped("1001", "Laura Gómez", "3001112233"));
        registrarHuesped(new Huesped("1002", "Carlos Ruiz", "3104445566"));
    }

    // --- GESTIÓN DE HUÉSPEDES (HashMap) ---
    public boolean registrarHuesped(Huesped huesped) {
        if (huesped == null || huespedes.containsKey(huesped.getDocumento())) {
            return false;
        }
        huespedes.put(huesped.getDocumento(), huesped);
        return true;
    }

    public Huesped buscarHuesped(String documento) {
        return huespedes.get(documento);
    }

    public List<Huesped> listarHuespedes() {
        return new ArrayList<>(huespedes.values());
    }

    // --- GESTIÓN DE HABITACIONES (HashSet + TreeSet) ---
    public boolean registrarHabitacion(Habitacion habitacion) {
        if (habitacion == null || !catalogoHabitaciones.add(habitacion)) {
            return false;
        }
        if (habitacion.isDisponible()) {
            habitacionesDisponibles.add(habitacion);
        }
        return true;
    }

    public Habitacion buscarHabitacion(int numero) {
        for (Habitacion h : catalogoHabitaciones) {
            if (h.getNumero() == numero) {
                return h;
            }
        }
        return null;
    }

    public List<Habitacion> listarTodasHabitaciones() {
        return new ArrayList<>(catalogoHabitaciones);
    }

    public List<Habitacion> listarDisponiblesPorPrecio() {
        return new ArrayList<>(habitacionesDisponibles);
    }

    // --- GESTIÓN DE RESERVAS (ArrayList + PriorityQueue) ---
    public String crearReserva(String codigo, String documentoHuesped, int numeroHabitacion, String fecha, String prioridad) {
        for (Reserva r : historialReservas) {
            if (r.getCodigo().equalsIgnoreCase(codigo)) {
                return "Ya existe una reserva con el código " + codigo + ".";
            }
        }

        Huesped huesped = buscarHuesped(documentoHuesped);
        if (huesped == null) {
            return "No existe un huésped registrado con documento " + documentoHuesped + ".";
        }

        Habitacion habitacion = buscarHabitacion(numeroHabitacion);
        if (habitacion == null) {
            return "La habitación #" + numeroHabitacion + " no existe.";
        }
        if (!habitacion.isDisponible()) {
            return "La habitación #" + numeroHabitacion + " ya se encuentra ocupada.";
        }

        Reserva nueva = new Reserva(codigo, huesped, habitacion, fecha, prioridad);
        habitacion.setDisponible(false);
        habitacionesDisponibles.remove(habitacion);

        historialReservas.add(nueva);
        colaAtencion.offer(nueva);
        return "OK";
    }

    public Reserva procesarSiguienteReserva() {
        Reserva siguiente = colaAtencion.poll();
        if (siguiente != null) {
            siguiente.setEstado("Confirmada");
        }
        return siguiente;
    }

    public boolean cancelarReserva(String codigo) {
        for (Reserva r : historialReservas) {
            if (r.getCodigo().equalsIgnoreCase(codigo) && !r.getEstado().equals("Cancelada")) {
                r.setEstado("Cancelada");
                colaAtencion.remove(r);
                Habitacion hab = r.getHabitacion();
                hab.setDisponible(true);
                habitacionesDisponibles.add(hab);
                return true;
            }
        }
        return false;
    }

    public List<Reserva> listarHistorialReservas() {
        return new ArrayList<>(historialReservas);
    }

    public List<Reserva> listarColaPrioridad() {
        PriorityQueue<Reserva> copia = new PriorityQueue<>(colaAtencion);
        List<Reserva> ordenadas = new ArrayList<>();
        while (!copia.isEmpty()) {
            ordenadas.add(copia.poll());
        }
        return ordenadas;
    }
}