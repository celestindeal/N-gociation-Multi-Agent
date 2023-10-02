package communication;

import Agent.Agent;

public class refuseMessage extends Message {

     private String body = "refuse";
    
    public refuseMessage(Agent sender, Agent receiver, String body) {
        super(sender, receiver);
        this.body = body;
    }


    public String getBody() {
        return body;
    }

 
  
}
