package communication;

import Agent.Agent;
import Agent.Service;

public class OfferMessage extends Message {
    
    public OfferMessage(Agent sender, Agent receiver,Offre offre) {
        super(sender, receiver, offre );
    }
  
}
