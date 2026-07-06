package modelos;

import java.io.Serializable;
import java.util.ArrayList;

public class ResultadoRecomendacion implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<Juego> juegosRecomendados;

    public ResultadoRecomendacion() {
        juegosRecomendados = new ArrayList<>();
    }

    public ArrayList<Juego> getJuegosRecomendados() {
        return juegosRecomendados;
    }

    public void setJuegosRecomendados(ArrayList<Juego> juegosRecomendados) {
        this.juegosRecomendados = juegosRecomendados;
    }

    public void agregarJuego(Juego juego) {
        juegosRecomendados.add(juego);
    }

    @Override
    public String toString() {
        return juegosRecomendados.toString();
    }

}