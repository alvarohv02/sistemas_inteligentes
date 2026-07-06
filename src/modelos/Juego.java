package modelos;

import java.io.Serializable;

public class Juego implements Serializable {

    private static final long serialVersionUID = 1L;

    private String titulo;
    private String genero;
    private String plataforma;
    private int anio;
    private double puntuacion;

    public Juego() {

    }

    public Juego(String titulo, String genero, String plataforma, int anio, double puntuacion) {
        this.titulo = titulo;
        this.genero = genero;
        this.plataforma = plataforma;
        this.anio = anio;
        this.puntuacion = puntuacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public double getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(double puntuacion) {
        this.puntuacion = puntuacion;
    }

    @Override
    public String toString() {
        return titulo + " | " + genero + " | " + plataforma + " | " + anio + " | ⭐ " + puntuacion;
    }

}