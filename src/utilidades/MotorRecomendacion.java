package utilidades;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import modelos.Juego;
import modelos.JuegoRecomendado;
import modelos.PeticionRecomendacion;
import modelos.ResultadoRecomendacion;

public class MotorRecomendacion {

    public static ResultadoRecomendacion recomendar(
            ArrayList<Juego> juegos,
            PeticionRecomendacion peticion) {

        ArrayList<JuegoRecomendado> listaPuntuada = new ArrayList<>();

        List<String> generosSeleccionados = peticion.getGenerosSeleccionados();
        if (generosSeleccionados == null) {
            generosSeleccionados = new ArrayList<>();
        }

        List<String> plataformasSeleccionadas = peticion.getPlataformasSeleccionadas();
        if (plataformasSeleccionadas == null) {
            plataformasSeleccionadas = new ArrayList<>();
        }

        if (generosSeleccionados.isEmpty() && peticion.getGenero() != null
                && !peticion.getGenero().trim().isEmpty()) {
            generosSeleccionados.add(peticion.getGenero().trim());
        }

        if (plataformasSeleccionadas.isEmpty() && peticion.getPlataforma() != null
                && !peticion.getPlataforma().trim().isEmpty()) {
            plataformasSeleccionadas.add(peticion.getPlataforma().trim());
        }

        for (Juego juego : juegos) {

            if (!generosSeleccionados.isEmpty()
                    && !contieneIgnorandoMayusculas(generosSeleccionados, juego.getGenero())) {
                continue;
            }

            if (!plataformasSeleccionadas.isEmpty()
                    && !contieneIgnorandoMayusculas(plataformasSeleccionadas, juego.getPlataforma())) {
                continue;
            }

            int puntos = 0;

            if (juego.getPuntuacion() >= peticion.getPuntuacionMinima()) {
                puntos += 2;
            }

            if (juego.getAnio() >= peticion.getAnioMinimo()) {
                puntos += 1;
            }

            if (juego.getPuntuacion() >= 9.5) {
                puntos += 1;
            }

            listaPuntuada.add(
                    new JuegoRecomendado(juego, puntos));

        }

        Collections.sort(listaPuntuada,
                new Comparator<JuegoRecomendado>() {

                    @Override
                    public int compare(
                            JuegoRecomendado j1,
                            JuegoRecomendado j2) {

                        if (j2.getPuntuacion() != j1.getPuntuacion()) {

                            return Integer.compare(
                                    j2.getPuntuacion(),
                                    j1.getPuntuacion());
                        }

                        return Double.compare(
                                j2.getJuego().getPuntuacion(),
                                j1.getJuego().getPuntuacion());

                    }

                });

        ResultadoRecomendacion resultado = new ResultadoRecomendacion();

        int limiteResultados = peticion.getMaxResultados() > 0
                ? peticion.getMaxResultados()
                : 5;

        for (int i = 0; i < listaPuntuada.size() && i < limiteResultados; i++) {

            resultado.agregarJuego(
                    listaPuntuada.get(i).getJuego());

        }

        return resultado;

    }

    private static boolean contieneIgnorandoMayusculas(
            List<String> opciones,
            String valor) {

        if (valor == null) {
            return false;
        }

        for (String opcion : opciones) {
            if (opcion != null && valor.equalsIgnoreCase(opcion.trim())) {
                return true;
            }
        }

        return false;
    }

}