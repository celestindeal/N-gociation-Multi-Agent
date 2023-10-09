package Agent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Strate.StrategiesFournisseur;
import Strate.StrategiesNegociateur;
import communication.Message;
import communication.OfferMessage;
import communication.placePublic;
import communication.refuseMessage;
import communication.valideMessage;

// Classe pour les agents négociateurs
public class AgentNegociateur extends Agent {

    private Double money = 0.0; 
    int nbNegociation = 0;
    private Map<Agent, Historique> historique = new HashMap<>();

    public AgentNegociateur(int agentID, List<Preference> preferences, List<Contrainte> contraintes, Double money) {
        super(agentID,"Negociateur", preferences, contraintes);
        this.money = money;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(2000); 
                if(nbNegociation < 1 && !placePublic.getInstance(null).getServiceAvendre().isEmpty()){
                    rechercheService();
                }
                if(!boiteAuxLettres.isEmpty()) {
                    traiterMessages(boiteAuxLettres.get(0));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private void traiterMessages(Message message) {
        System.out.println("NEGOCIATEUR " + this.agentID + " : Je traite un message du fournisseur " + message.getSender().getAgentID());
         if (message instanceof OfferMessage) {
            StrategiesNegociateur.strategieOffreMessage(this, (OfferMessage) message, historique.get(message.getSender()));
        } else if (message instanceof refuseMessage) {
            System.out.println("NEGOCIATEUR " + this.agentID + " : l'offre pour le service" + message.getOffer().getService().getServiceID() + "que j'ai envoyé a " + message.getSender().getAgentID() + " a été refusé");
            
            //supprimer l'hisorique de l'agent
            historique.remove(message.getSender());

        } else if (message instanceof valideMessage) {
            System.out.println(".................NEGOCIATEUR " + this.agentID + " : l'offre pour le service" + message.getOffer().getService().getServiceID() + "que j'ai envoyé a " + message.getSender().getAgentID() + " a été accepté");

            historique.remove(message.getSender());
            nbNegociation --;
        } else {
            // Message non reconnu
            System.out.println("Message non reconnu : " + message.toString());
        }
        boiteAuxLettres.remove(message);
    }

    public List<Preference> getPreferencesUtilisateur() {
        return preferences;
    }

    public void setPreferencesUtilisateur(List<Preference> preferencesUtilisateur) {
        this.preferences = preferencesUtilisateur;
    }

    public List<Contrainte> getContraintesUtilisateur() {
        return contraintes;
    }

    public void setContraintesUtilisateur(List<Contrainte> contraintesUtilisateur) {
        this.contraintes = contraintesUtilisateur;
    }

    // fonction pour recherche un service dans la liste des services
    public void rechercheService() {
        // prend un service aléatoire dans la liste des services et envoyer une offre 
        System.out.println("NEGOCIATEUR " + this.agentID + " : Je recherche un service //// Il y a " + placePublic.getInstance(null).getServiceAvendre().size() + " services en vente");
        Service service = placePublic.getInstance(null).getServiceAvendre().get((int) (Math.random() * placePublic.getInstance(null).getServiceAvendre().size()));
        OfferMessage offre = new OfferMessage(this, service.getAgentFournisseur(), service, service.getPrix());
        System.out.println("NEGOCIATEUR " + this.agentID + "j'envoie une offre au fournisseur = " + service.getAgentFournisseur().getAgentID() + " pour le service : " + service.getServiceID() );
        sendMessage(offre);
        nbNegociation ++;
    }



}

