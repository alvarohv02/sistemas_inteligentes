	package comportamientos;
	
	import jade.core.behaviours.CyclicBehaviour;
	
	import jade.lang.acl.ACLMessage;
	import jade.lang.acl.MessageTemplate;
	import java.io.IOException;
	import java.util.ArrayList;
	import modelos.Juego;
	
	
	public class PeticionDatosBehaviour extends CyclicBehaviour {
		
	
		public PeticionDatosBehaviour(ArrayList<Juego> juegos) {
		    this.juegos = juegos;
		}
		private ArrayList<Juego> juegos;
		
		
	    @Override
	    public void action() {
	
	        ACLMessage mensaje =
	                myAgent.blockingReceive(
	                        MessageTemplate.MatchPerformative(
	                                ACLMessage.REQUEST));
	
	        if (mensaje != null) {
	
	            System.out.println("--------------------------------");
	            System.out.println("AgenteDatos ha recibido una petición.");
	
	            ACLMessage respuesta = mensaje.createReply();
	
	            respuesta.setPerformative(ACLMessage.INFORM);
	
	            try {
	
	                respuesta.setContentObject(juegos);
	
	            } catch (IOException e) {
	
	                e.printStackTrace();
	
	            }
	
	            myAgent.send(respuesta);
	
	            System.out.println("Enviando " + juegos.size() + " videojuegos.");
	            System.out.println("--------------------------------");
	        }
	
	    }}