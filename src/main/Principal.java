package main;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import java.io.IOException;
import java.net.ServerSocket;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class Principal {

    private static final int[] PUERTOS_CANDIDATOS = { 1099, 1100, 1200, 1300 };
    private static int puertoActivo = -1;

    public static void main(String[] args) {

        try {
            Runtime runtime = Runtime.instance();

            boolean mostrarRma = contieneArgumento(args, "--rma");

            ContainerController contenedor = crearContenedorPrincipal(runtime, mostrarRma);

            if (contenedor == null) {
                System.err.println("No se pudo iniciar el contenedor principal de JADE.");
                System.err.println("Cierra otras instancias de JADE o libera los puertos 1099/1100/1200/1300.");
                return;
            }

            AgentController agenteDatos = contenedor.createNewAgent(
                    "datos",
                    "agentes.AgenteDatos",
                    null);

            agenteDatos.start();

            Thread.sleep(500);

            AgentController agenteRecomendador = contenedor.createNewAgent(
                    "recomendador",
                    "agentes.AgenteRecomendador",
                    null);

            agenteRecomendador.start();

            Thread.sleep(500);

            AgentController agenteUsuario = contenedor.createNewAgent(
                    "usuario",
                    "agentes.AgenteUsuario",
                    null);

            agenteUsuario.start();

            System.out.println("[Resumen] Plataforma iniciada en puerto " + puertoActivo
                    + " | agentes: datos, recomendador, usuario"
                    + " | RMA: " + (mostrarRma ? "habilitada" : "deshabilitada"));

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    private static ContainerController crearContenedorPrincipal(Runtime runtime, boolean mostrarRma) {

        for (int puerto : PUERTOS_CANDIDATOS) {
            if (!puertoDisponible(puerto)) {
                System.err.println("Puerto " + puerto + " ocupado. Probando otro...");
                continue;
            }

            try {
                Profile profile = new ProfileImpl();
                profile.setParameter(Profile.GUI, String.valueOf(mostrarRma));
                profile.setParameter(Profile.LOCAL_PORT, String.valueOf(puerto));

                ContainerController contenedor = runtime.createMainContainer(profile);

                if (contenedor == null) {
                    System.err.println("JADE devolvio contenedor nulo en puerto " + puerto + ".");
                    continue;
                }

                puertoActivo = puerto;
                return contenedor;
            } catch (Exception e) {
                System.err.println("No se pudo iniciar JADE en puerto " + puerto + ". Probando otro...");
            }
        }

        return null;
    }

    private static boolean puertoDisponible(int puerto) {
        try (ServerSocket socket = new ServerSocket(puerto)) {
            socket.setReuseAddress(true);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static boolean contieneArgumento(String[] args, String objetivo) {
        if (args == null || objetivo == null) {
            return false;
        }

        for (String arg : args) {
            if (objetivo.equalsIgnoreCase(arg)) {
                return true;
            }
        }

        return false;
    }

}