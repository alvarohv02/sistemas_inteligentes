package utilidades;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;

import modelos.Juego;

public class LectorJSON {

    public static ArrayList<Juego> cargarJuegos() {

        try {

            Gson gson = new Gson();

            Reader reader = new FileReader("resources/juegos.json");

            Juego[] juegos = gson.fromJson(reader, Juego[].class);

            reader.close();

            return new ArrayList<>(Arrays.asList(juegos));

        } catch (Exception e) {

            e.printStackTrace();

            return new ArrayList<>();

        }

    }

}