package communication;

import Agent.Agent;
import Agent.Service;

public class OfferMessage extends Message {
    
    public OfferMessage(Agent sender, Agent receiver,Service service, double offerAmount) {
        super(sender, receiver, new Offre(service, offerAmount) );
    }
  
}
