package Agent;

import Communication.Message;

import java.util.ArrayList;
import java.util.List;

// Classe de base pour tous les agents
public abstract class Agent extends Thread {

    protected int agentID;
    protected String agentType;
    protected List<Preference> preferences;
    protected List<Contrainte> contraintes;

    protected ArrayList<Message> boiteAuxLettres = new ArrayList<Message>();
    ;

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

    //////////////// Gestion de la communication entre agents

    public void sendMessage(Message message) {
        this.log("J'envoie un message à [" + message.getReceiver().getAgentType() + " " + message.getReceiver().getAgentID() + "] - Le message est de type " + this.getMessageLog(message));
        message.getReceiver().receiveMessage(message);
    }

    // Ajouter un message à la boîte aux lettres
    public void receiveMessage(Message message) {
        this.log("Je reçois un message de [" + message.getSender().getAgentType() + " " + message.getSender().getAgentID() + "] - Le message est de type " + this.getMessageLog(message));
        boiteAuxLettres.add(message);
    }

    public void log(String message) {
        System.out.println("[" + this.agentType + " " + this.agentID + "]\t\t\t\t" + message);
    }

    private String getMessageLog(Message message) {
        switch (message.getClass().getSimpleName()) {
            case "MessageOffre":
                return "MessageOffre - Service " + message.getOffer().getService().getServiceID() + " - Prix " + message.getOffer().getPrix();
            case "MessageRefus":
                return "MessageRefus - Service " + message.getOffer().getService().getServiceID();
            case "MessageValide":
                return "MessageValide - Service " + message.getOffer().getService().getServiceID() + " - Prix " + message.getOffer().getPrix();
            default:
                return "Message non reconnu";
        }
    }
}


