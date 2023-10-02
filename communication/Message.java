package communication;

import Agent.Agent;

public abstract class Message {
    private Agent sender;
    private Agent receiver;

    public Message(Agent sender, Agent receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public Agent getSender() {
        return sender;
    }

    public Agent getReceiver() {
        return receiver;
    }

}