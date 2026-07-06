package modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PeticionRecomendacion implements Serializable {

    private static final long serialVersionUID = 1L;

    private String genero;
    private String plataforma;
    private ArrayList<String> generosSeleccionados;
    private ArrayList<String> plataformasSeleccionadas;
    private double puntuacionMinima;
    private int anioMinimo;
    private int maxResultados;

    public PeticionRecomendacion() {
        generosSeleccionados = new ArrayList<>();
        plataformasSeleccionadas = new ArrayList<>();
        maxResultados = 5;
    }

    public PeticionRecomendacion(String genero, String plataforma,
            double puntuacionMinima, int anioMinimo) {

        this.genero = genero;
        this.plataforma = plataforma;
        this.generosSeleccionados = new ArrayList<>();
        this.plataformasSeleccionadas = new ArrayList<>();
        this.puntuacionMinima = puntuacionMinima;
        this.anioMinimo = anioMinimo;
        this.maxResultados = 5;
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

    public ArrayList<String> getGenerosSeleccionados() {
        return generosSeleccionados;
    }

    public void setGenerosSeleccionados(List<String> generosSeleccionados) {
        this.generosSeleccionados = new ArrayList<>();

        if (generosSeleccionados != null) {
            this.generosSeleccionados.addAll(generosSeleccionados);
        }
    }

    public ArrayList<String> getPlataformasSeleccionadas() {
        return plataformasSeleccionadas;
    }

    public void setPlataformasSeleccionadas(List<String> plataformasSeleccionadas) {
        this.plataformasSeleccionadas = new ArrayList<>();

        if (plataformasSeleccionadas != null) {
            this.plataformasSeleccionadas.addAll(plataformasSeleccionadas);
        }
    }

    public double getPuntuacionMinima() {
        return puntuacionMinima;
    }

    public void setPuntuacionMinima(double puntuacionMinima) {
        this.puntuacionMinima = puntuacionMinima;
    }

    public int getAnioMinimo() {
        return anioMinimo;
    }

    public void setAnioMinimo(int anioMinimo) {
        this.anioMinimo = anioMinimo;
    }

    public int getMaxResultados() {
        return maxResultados;
    }

    public void setMaxResultados(int maxResultados) {
        if (maxResultados > 0) {
            this.maxResultados = maxResultados;
        }
    }

}