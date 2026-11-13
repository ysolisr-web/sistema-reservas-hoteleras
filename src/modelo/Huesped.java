package modelo;

import java.util.Objects;

public class Huesped {
    private String documento;
    private String nombre;
    private String telefono;

    public Huesped(String documento, String nombre, String telefono) {
        this.documento = documento;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Huesped)) return false;
        Huesped huesped = (Huesped) o;
        return Objects.equals(documento, huesped.documento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documento);
    }

    @Override
    public String toString() {
        return nombre + " (Doc: " + documento + ", Tel: " + telefono + ")";
    }
}
