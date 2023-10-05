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
    }

    

    // Fonction de gestion des messages
    public void traiterMessages(Message message) {
        System.out.println("FOURNISEUR " + this.agentID + " : Je traite un message de " + message.getSender().getAgentID());

        // contrôler si l'agent est autorisé à négocier le service
        if(!autoriserNegociationThisService(message.getSender(), message.getOffer().getService())) {
            System.out.println("FOURNISEUR " + this.agentID + " : l'agent " + message.getSender().getAgentID() + " n'est pas autorisé à négocier le service " + message.getOffer().getService().getServiceID());
            // envoyer un message de refus
            this.sendMessage(new refuseMessage(this, message.getSender(), message.getOffer()));
            return;
        }

        if (message instanceof OfferMessage) {
            StrategiesFournisseur.strategieOffreMessage(this, (OfferMessage) message, historique.get(message.getSender()));
        } else if (message instanceof refuseMessage) {
            System.out.println("FOURNISEUR " + this.agentID + " : je refuse l'offre de " + message.getSender().getAgentID() + " pour le service " + message.getOffer().getService().getServiceID() + " à " + message.getOffer().getPrix() + "€");
            
            //supprimer l'hisorique de l'agent
            historique.remove(message.getSender());

            placePublic.getInstance(null).removeService( message.getOffer().getService() );

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

    //  on autorise la negociation d'un service avec un seul agent à la fois
    private boolean autoriserNegociationThisService(Agent agent, Service service) {

        // si l'agent négocie déjà une autre offret on refuse la negociation
        if(historique.containsKey(agent) && !historique.get(agent).getOffreNegociateur().get(0).getService().equals(service)) {
            // l'agent a déjà negocier ce service
            return false;
        }

        //Savoir si le service est déjà negocier 
        for(Historique h : historique.values()) {
            if(h.getOffreNegociateur().get(0).getService().equals(service)) {
                // le service est déjà negocier
                // si c'est parle le même agent on autorise la negociation
                if(h.getOffreNegociateur().get(0).equals(agent)) {
                    return true;
                } else {
                    // sinon on refuse la negociation
                    return false;
                }
            }
        }
        // le service n'est pas encore negocier
        return true;
        
    }
}
