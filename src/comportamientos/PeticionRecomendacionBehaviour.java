package comportamientos;

import java.io.IOException;
import java.util.ArrayList;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import modelos.Juego;
import modelos.PeticionRecomendacion;
import modelos.ResultadoRecomendacion;
import utilidades.MotorRecomendacion;

public class PeticionRecomendacionBehaviour extends CyclicBehaviour {

    @Override
    public void action() {

        ACLMessage mensaje = myAgent.blockingReceive(
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST));

        if (mensaje == null) {
            return;
        }

        try {

            PeticionRecomendacion peticion = (PeticionRecomendacion) mensaje.getContentObject();

            System.out.println("--------------------------------");
            System.out.println("Petición recibida.");

            System.out.println("Generos: " + peticion.getGenerosSeleccionados());
            System.out.println("Plataformas: " + peticion.getPlataformasSeleccionadas());
            System.out.println("Puntuacion minima: " + peticion.getPuntuacionMinima());
            System.out.println("Año minimo: " + peticion.getAnioMinimo());

            ArrayList<Juego> juegos = solicitarCatalogoDesdeAgenteDatos();

            if (juegos.isEmpty()) {
                System.out.println("No se pudieron recuperar juegos desde el AgenteDatos.");
            }

            ResultadoRecomendacion resultado = MotorRecomendacion.recomendar(juegos, peticion);

            ACLMessage respuesta = mensaje.createReply();

            respuesta.setPerformative(ACLMessage.INFORM);

            respuesta.setContentObject(resultado);

            myAgent.send(respuesta);

            System.out.println("Recomendación enviada al usuario.");
            System.out.println("--------------------------------");

        } catch (IOException e) {

            e.printStackTrace();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    private ArrayList<Juego> solicitarCatalogoDesdeAgenteDatos() {

        try {
            AID agenteDatos = buscarAgenteDatos();

            if (agenteDatos == null) {
                return new ArrayList<>();
            }

            ACLMessage solicitud = new ACLMessage(ACLMessage.REQUEST);
            solicitud.addReceiver(agenteDatos);

            String conversationId = "solicitud-datos-" + System.currentTimeMillis();
            solicitud.setConversationId(conversationId);
            myAgent.send(solicitud);

            MessageTemplate plantillaRespuestaDatos = MessageTemplate.and(
                    MessageTemplate.MatchConversationId(conversationId),
                    MessageTemplate.MatchSender(agenteDatos));

            ACLMessage respuestaDatos = myAgent.blockingReceive(plantillaRespuestaDatos, 3000);

            if (respuestaDatos == null
                    || respuestaDatos.getPerformative() != ACLMessage.INFORM) {
                return new ArrayList<>();
            }

            @SuppressWarnings("unchecked")
            ArrayList<Juego> juegos = (ArrayList<Juego>) respuestaDatos.getContentObject();
            return juegos;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private AID buscarAgenteDatos() throws FIPAException {

        DFAgentDescription plantilla = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("datos-videojuegos");
        plantilla.addServices(sd);

        DFAgentDescription[] resultados = DFService.search(myAgent, plantilla);

        if (resultados.length == 0) {
            return null;
        }

        return resultados[0].getName();
    }

}