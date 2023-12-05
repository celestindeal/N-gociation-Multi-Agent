package Communication;

import Agent.Agent;

public class MessageOffre extends Message {

    public MessageOffre(Agent sender, Agent receiver, Offre offre) {
        super(sender, receiver, offre);
    }

}
