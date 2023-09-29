package Agent;

import java.util.List;

import communication.Message;
import communication.OfferMessage;
import communication.RequestMessage;
import communication.placePublic;

// Classe de base pour tous les agents
public abstract class Agent {

    private placePublic placePublic;


    protected String agentID;
    protected String agentType;

    protected List<Preference> preferences;
    protected List<Contrainte> contraintes;

    public Agent(String agentID, String agentType, List<Preference> preferencesUtilisateur, List<Contrainte> contraintesUtilisateur) {
        this.agentID = agentID;
        this.agentType = agentType;
        this.preferences = preferencesUtilisateur;
        this.contraintes = contraintesUtilisateur;
    }



    //////////////// Getter pour les attributs

    public String getAgentID() {
        return agentID;
    }

    public String getAgentType() {
        return agentType;
    }

    //////////////// Gestion des la communication entre agents

     public void sendMessage(Message message) {
        message.getReceiver().receiveMessage(message);
    }

    public void receiveMessage(Message message) {
    }

    protected void handleOffer(OfferMessage offerMessage) {
        // Logique pour traiter l'offre
    }

    protected void handleRequest(RequestMessage requestMessage) {
        // Logique pour traiter la demande
    }
}


