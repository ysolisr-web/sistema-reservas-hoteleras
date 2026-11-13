package modelo;

import java.util.Objects;

public class Habitacion {
    private int numero;
    private String tipo; // Sencilla, Doble, Suite
    private double tarifaPorNoche;
    private boolean disponible;

    public Habitacion(int numero, String tipo, double tarifaPorNoche, boolean disponible) {
        this.numero = numero;
        this.tipo = tipo;
        this.tarifaPorNoche = tarifaPorNoche;
        this.disponible = disponible;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getTarifaPorNoche() {
        return tarifaPorNoche;
    }

    public void setTarifaPorNoche(double tarifaPorNoche) {
        this.tarifaPorNoche = tarifaPorNoche;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Habitacion)) return false;
        Habitacion that = (Habitacion) o;
        return numero == that.numero;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }

    @Override
    public String toString() {
        return "Hab #" + numero + " [" + tipo + " - $" + tarifaPorNoche + "]";
    }
}