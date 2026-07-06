package agentes;

import jade.core.Agent;

import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import comportamientos.PeticionDatosBehaviour;
import java.util.ArrayList;
import modelos.Juego;
import utilidades.LectorJSON;

public class AgenteDatos extends Agent {

    private ArrayList<Juego> juegos;

    @Override
    protected void setup() {

        juegos = LectorJSON.cargarJuegos();

        // Registro del servicio en el Directory Facilitator
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setType("datos-videojuegos");
        sd.setName("ServicioDatosVideojuegos");

        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        System.out.println("[AgenteDatos] listo | catalogo cargado: " + juegos.size() + " juegos.");

        addBehaviour(new PeticionDatosBehaviour(juegos));
    }

    @Override
    protected void takeDown() {

        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        System.out.println(getLocalName() + " finalizado.");
    }

}