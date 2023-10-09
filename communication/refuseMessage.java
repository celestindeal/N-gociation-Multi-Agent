package communication;

import Agent.Agent;
import Agent.Service;

public class refuseMessage extends Message {
    
    public refuseMessage(Agent sender, Agent receiver, Service service) {
        super(sender, receiver, new Offre(service, 0.0));
    }
  
}
