package Agent;

import java.util.ArrayList;
import java.util.List;


import communication.Message;
import communication.OfferMessage;

// Classe de base pour tous les agents
public abstract class Agent extends Thread{

    protected int agentID;
    protected String agentType;

    protected List<Preference> preferences;
    protected List<Contrainte> contraintes;
    
    protected ArrayList<Message> boiteAuxLettres  = new ArrayList<Message>();;

    public Agent(int agentID, String agentType, List<Preference> preferencesUtilisateur, List<Contrainte> contraintesUtilisateur) {
        this.agentID = agentID;
        this.agentType = agentType;
        this.preferences = preferencesUtilisateur;
        this.contraintes = contraintesUtilisateur;
    }



    //////////////// Getter pour les attributs

    public int getAgentID() {
        return agentID;
    }

    public String getAgentType() {
        return agentType;
    }

    //////////////// Gestion des la communication entre agents

     public void sendMessage(Message message) {
        message.getReceiver().receiveMessage(message);
    }

    // Ajouter un message à la boîte aux lettres
    public void receiveMessage(Message message) {
        boiteAuxLettres.add(message);
    }

    

}


