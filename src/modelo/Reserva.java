package modelo;

import java.util.Objects;

public class Reserva implements Comparable<Reserva> {
    private String codigo;
    private Huesped huesped;
    private Habitacion habitacion;
    private String fecha;     // Formato YYYY-MM-DD
    private String prioridad; // VIP, ALTA, NORMAL
    private String estado;    // Pendiente, Confirmada, Cancelada

    public Reserva(String codigo, Huesped huesped, Habitacion habitacion, String fecha, String prioridad) {
        this.codigo = codigo;
        this.huesped = huesped;
        this.habitacion = habitacion;
        this.fecha = fecha;
        this.prioridad = prioridad.toUpperCase();
        this.estado = "Pendiente";
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Huesped getHuesped() {
        return huesped;
    }

    public void setHuesped(Huesped huesped) {
        this.huesped = huesped;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad.toUpperCase();
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Orden natural por código para TreeSet<Reserva>
    @Override
    public int compareTo(Reserva otra) {
        return this.codigo.compareToIgnoreCase(otra.codigo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reserva)) return false;
        Reserva reserva = (Reserva) o;
        return Objects.equals(codigo, reserva.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return String.format("[%s] Fecha: %s | Prioridad: %-6s | Estado: %-10s | Huésped: %s | %s",
                codigo, fecha, prioridad, estado, huesped.getNombre(), habitacion);
    }
}