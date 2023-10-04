package Agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Strate.StrategiesFournisseur;
import communication.Message;
import communication.OfferMessage;
import communication.placePublic;
import communication.refuseMessage;
import communication.valideMessage;

public class AgentFournisseur extends Agent {

    private Map<Agent, Historique> historique = new HashMap<>();

    public AgentFournisseur(int agentID, List<Preference> preferences, List<Contrainte> contraintes) {
        super(agentID, "Fournisseur", preferences, contraintes);
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (placePublic.getInstance(null).getserviceVenduFournisseur(this).isEmpty()) {
                    proposerService();
                }
                if(!boiteAuxLettres.isEmpty()) {
                    traiterMessages(boiteAuxLettres.get(0));
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private void proposerService() {
        // Choisir un service aléatoirement
        ArrayList<Service> services = placePublic.getInstance(null).getServicesPossible();
        int serviceID = (int) (Math.random() * services.size());

        Random random = new Random();
        double valeurAleatoire = random.nextInt(800) + 200;

        Service service = new Service(serviceID, this, valeurAleatoire);    // créer un nouveau service

        // proposer le service sur la place public
        placePublic.getInstance(null).buyService(service);

        System.out.println("Fournisseur " + service.getAgentFournisseur().getAgentID() + " : Je propose le service " + serviceID + " au prix de " + service.getPrix() + "€");
    }

    

    // Fonction de gestion des messages
    public void traiterMessages(Message message) {
        System.out.println("Fourniseur " + this.agentID + " : Je traite un message de " + message.getSender().getAgentID());
        if (message instanceof OfferMessage) {
            StrategiesFournisseur.strategieOffreMessage(this, (OfferMessage) message, historique.get(message.getSender()));
        } else if (message instanceof refuseMessage) {
            System.out.println("Fournisseur " + this.agentID + " : l'offre de " + message.getSender().getAgentID() + " a été refusée");
            
            //supprimer l'hisorique de l'agent
            historique.remove(message.getSender());

        } else if (message instanceof valideMessage) {
            System.out.println("Fournisseur " + this.agentID + " : l'offre de " + message.getSender().getAgentID() + " a été acceptée");

            //supprimer l'hisorique de l'agent et le service de la place public
            historique.remove(message.getSender());
            placePublic.getInstance(null).removeService( message.getOffer().getService() );
        } else {
            // Message non reconnu
            System.out.println("Message non reconnu : " + message.toString());
        }
        

        // Effacer les messages traités de la boîte aux lettres
        boiteAuxLettres.remove(message);
    }
}
