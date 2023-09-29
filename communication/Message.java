package communication;

import Agent.Agent;

public abstract class Message {
    private Agent sender;
    private Agent receiver;
    private String messageType;

    public Message(Agent sender, Agent receiver, String messageType) {
        this.sender = sender;
        this.receiver = receiver;
        this.messageType = messageType;
    }

    public Agent getSender() {
        return sender;
    }

    public Agent getReceiver() {
        return receiver;
    }

    public String getMessageType() {
        return messageType;
    }
}