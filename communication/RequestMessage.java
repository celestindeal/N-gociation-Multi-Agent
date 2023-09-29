package communication;

import Agent.Agent;

// Exemple de message de demande
public class RequestMessage extends Message {
    private String requestDetails;

    public RequestMessage(Agent sender, Agent receiver, String requestDetails) {
        super(sender, receiver, "REQUEST");
        this.requestDetails = requestDetails;
    }

    public String getRequestDetails() {
        return requestDetails;
    }
}
