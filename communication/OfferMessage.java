package communication;

import Agent.Agent;
import Agent.Service;

public class OfferMessage extends Message {
    
    private Offre offre;

    public OfferMessage(Agent sender, Agent receiver,Service service, double offerAmount) {
        super(sender, receiver );
        offre = new Offre(service, offerAmount);
    }

    public Offre getOffre() {
        return offre;
    }
  
}
