package Communication;

import Agent.Agent;
import Agent.Service;

public class MessageRefus extends Message {
    public MessageRefus(Agent sender, Agent receiver, Service service) {
        super(sender, receiver, new Offre(service, 0.0));
    }
}
