package communication;

import Agent.Agent;

public class OfferMessage extends Message {
    private double offerAmount;

    public OfferMessage(Agent sender, Agent receiver, double offerAmount) {
        super(sender, receiver, "OFFER");
        this.offerAmount = offerAmount;
    }

    public double getOfferAmount() {
        return offerAmount;
    }
}
