package agentes;

import jade.core.Agent;

import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import comportamientos.PeticionRecomendacionBehaviour;

public class AgenteRecomendador extends Agent {

    @Override
    protected void setup() {

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setType("recomendacion-videojuegos");
        sd.setName("ServicioRecomendacion");

        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        addBehaviour(new PeticionRecomendacionBehaviour());

        System.out.println("[AgenteRecomendador] listo.");

    }

    @Override
    protected void takeDown() {

        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        System.out.println(getLocalName() + "finalizado");
    }

}