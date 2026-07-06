package agentes;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import modelos.PeticionRecomendacion;
import modelos.ResultadoRecomendacion;
import modelos.Juego;

public class AgenteUsuario extends Agent {

    private JFrame ventana;
    private JList<String> listaGeneros;
    private JList<String> listaPlataformas;
    private JTextField campoPuntuacion;
    private JTextField campoAnio;
    private JComboBox<Integer> comboMaxResultados;
    private JTextArea areaResultados;
    private JButton botonRecomendar;

    @Override
    protected void setup() {

        System.out.println("Iniciando " + getLocalName());

        addBehaviour(new RecepcionResultadoBehaviour());

        SwingUtilities.invokeLater(this::crearInterfaz);
    }

    private void crearInterfaz() {

        ventana = new JFrame("Recomendador de Videojuegos");
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setSize(760, 500);
        ventana.setMinimumSize(new java.awt.Dimension(700, 420));
        ventana.setLocationRelativeTo(null);

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Preferencias"));

        JPanel panelSeleccionables = new JPanel(new GridLayout(1, 2, 10, 0));
        panelSeleccionables.setBorder(BorderFactory.createEmptyBorder(8, 8, 0, 8));

        listaGeneros = new JList<>(new String[] {
                "RPG", "Accion", "Aventura", "Shooter",
                "Deportes", "Carreras", "Estrategia", "Terror", "Indie"
        });
        listaGeneros.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaGeneros.setVisibleRowCount(5);
        listaGeneros.setSelectedIndices(new int[] { 0 });

        listaPlataformas = new JList<>(new String[] {
                "PC", "PlayStation", "Xbox", "Nintendo Switch"
        });
        listaPlataformas.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaPlataformas.setVisibleRowCount(5);
        listaPlataformas.setSelectedIndices(new int[] { 0 });

        JScrollPane scrollGeneros = new JScrollPane(listaGeneros);
        scrollGeneros.setBorder(BorderFactory.createTitledBorder("Generos (multi-seleccion)"));

        JScrollPane scrollPlataformas = new JScrollPane(listaPlataformas);
        scrollPlataformas.setBorder(BorderFactory.createTitledBorder("Plataformas (multi-seleccion)"));

        panelSeleccionables.add(scrollGeneros);
        panelSeleccionables.add(scrollPlataformas);

        campoPuntuacion = new JTextField("8.5", 5);
        campoAnio = new JTextField("2020", 6);
        comboMaxResultados = new JComboBox<>(new Integer[] { 5, 10, 15, 20, 50 });
        comboMaxResultados.setSelectedItem(5);

        botonRecomendar = new JButton("Obtener recomendaciones");
        botonRecomendar.addActionListener(e -> enviarPeticionDesdeFormulario());

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        panelAcciones.add(botonRecomendar);

        panelFiltros.add(new JLabel("Puntuacion minima:"));
        panelFiltros.add(campoPuntuacion);
        panelFiltros.add(new JLabel("Año minimo:"));
        panelFiltros.add(campoAnio);
        panelFiltros.add(new JLabel("Max resultados:"));
        panelFiltros.add(comboMaxResultados);

        panelSuperior.add(panelSeleccionables, BorderLayout.NORTH);
        panelSuperior.add(panelFiltros, BorderLayout.CENTER);
        panelSuperior.add(panelAcciones, BorderLayout.SOUTH);

        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setLineWrap(true);
        areaResultados.setWrapStyleWord(true);
        areaResultados.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JScrollPane scroll = new JScrollPane(areaResultados);
        scroll.setBorder(BorderFactory.createTitledBorder("Resultados"));

        ventana.getContentPane().setLayout(new BorderLayout());
        ventana.getContentPane().add(panelSuperior, BorderLayout.NORTH);
        ventana.getContentPane().add(scroll, BorderLayout.CENTER);

        ventana.setVisible(true);
    }

    private void enviarPeticionDesdeFormulario() {

        double puntuacion;
        int anio;
        ArrayList<String> generosSeleccionados = new ArrayList<>(listaGeneros.getSelectedValuesList());
        ArrayList<String> plataformasSeleccionadas = new ArrayList<>(listaPlataformas.getSelectedValuesList());

        if (generosSeleccionados.isEmpty() || plataformasSeleccionadas.isEmpty()) {
            JOptionPane.showMessageDialog(
                    ventana,
                    "Debes seleccionar al menos un genero y una plataforma.",
                    "Filtros incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            puntuacion = Double.parseDouble(campoPuntuacion.getText().trim());
            anio = Integer.parseInt(campoAnio.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    ventana,
                    "Puntuacion y anio deben ser numericos.",
                    "Datos invalidos",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            AID receptor = buscarAgenteRecomendador();

            if (receptor == null) {
                areaResultados.setText("No se encontro el servicio de recomendacion.");
                return;
            }

            PeticionRecomendacion peticion = new PeticionRecomendacion();
            peticion.setGenerosSeleccionados(generosSeleccionados);
            peticion.setPlataformasSeleccionadas(plataformasSeleccionadas);
            peticion.setGenero(generosSeleccionados.get(0));
            peticion.setPlataforma(plataformasSeleccionadas.get(0));
            peticion.setPuntuacionMinima(puntuacion);
            peticion.setAnioMinimo(anio);
            peticion.setMaxResultados((Integer) comboMaxResultados.getSelectedItem());

            ACLMessage mensaje = new ACLMessage(ACLMessage.REQUEST);
            mensaje.addReceiver(receptor);
            mensaje.setConversationId("peticion-recomendacion-ui");
            mensaje.setContentObject(peticion);

            send(mensaje);

            areaResultados.setText("Buscando recomendaciones...\n");
            botonRecomendar.setEnabled(false);
        } catch (Exception e) {
            areaResultados.setText("Error enviando peticion: " + e.getMessage());
            botonRecomendar.setEnabled(true);
        }
    }

    private AID buscarAgenteRecomendador() throws Exception {

        DFAgentDescription plantilla = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("recomendacion-videojuegos");
        plantilla.addServices(sd);

        DFAgentDescription[] resultados = DFService.search(this, plantilla);

        if (resultados.length == 0) {
            return null;
        }

        return resultados[0].getName();
    }

    @Override
    protected void takeDown() {
        SwingUtilities.invokeLater(() -> {
            if (ventana != null) {
                ventana.dispose();
            }
        });
    }

    private class RecepcionResultadoBehaviour extends CyclicBehaviour {

        @Override
        public void action() {

            MessageTemplate plantilla = MessageTemplate.and(
                    MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                    MessageTemplate.MatchConversationId("peticion-recomendacion-ui"));

            ACLMessage respuesta = myAgent.receive(plantilla);

            if (respuesta == null) {
                block();
                return;
            }

            try {
                ResultadoRecomendacion resultado = (ResultadoRecomendacion) respuesta.getContentObject();

                SwingUtilities.invokeLater(() -> mostrarResultado(resultado));
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    areaResultados.setText("Error al leer respuesta: " + e.getMessage());
                    botonRecomendar.setEnabled(true);
                });
            }
        }
    }

    private void mostrarResultado(ResultadoRecomendacion resultado) {

        StringBuilder sb = new StringBuilder();
        sb.append("Recomendaciones encontradas:\n\n");

        if (resultado.getJuegosRecomendados().isEmpty()) {
            sb.append("No se han encontrado coincidencias con los filtros seleccionados");
        } else {
            int posicion = 1;
            for (Juego juego : resultado.getJuegosRecomendados()) {
                sb.append(posicion)
                        .append(". ")
                        .append(juego.toString())
                        .append("\n");
                posicion++;
            }
        }

        areaResultados.setText(sb.toString());
        botonRecomendar.setEnabled(true);
    }
}