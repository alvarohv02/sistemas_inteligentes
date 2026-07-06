package modelos;

public class JuegoRecomendado {

    private Juego juego;
    private int puntuacion;

    public JuegoRecomendado(Juego juego, int puntuacion) {

        this.juego = juego;
        this.puntuacion = puntuacion;

    }

    public Juego getJuego() {
        return juego;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

}