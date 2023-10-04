package communication;

import Agent.Agent;

public class refuseMessage extends Message {
    
    public refuseMessage(Agent sender, Agent receiver, Offre offre) {
        super(sender, receiver, offre);
    }
  
}
