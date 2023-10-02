package communication;

import Agent.Agent;

public class valideMessage extends Message {

     private String body = "accepte";
    
    public valideMessage(Agent sender, Agent receiver, String body) {
        super(sender, receiver);
        this.body = body;
    }




    public String getBody() {
        return body;
    }

 
  
}
