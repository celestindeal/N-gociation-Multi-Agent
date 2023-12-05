package Communication;

import Agent.Agent;

public abstract class Message {
    private Agent sender;
    private Agent receiver;
    private Offre offre;


    public Message(Agent sender, Agent receiver, Offre offre) {
        this.sender = sender;
        this.receiver = receiver;
        this.offre = offre;
    }

    public Agent getSender() {
        return sender;
    }

    public Agent getReceiver() {
        return receiver;
    }

    public Offre getOffer() {
        return offre;
    }

}