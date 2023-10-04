package Strate;

import Agent.AgentFournisseur;
import Agent.Historique;
import communication.OfferMessage;
import communication.placePublic;
import communication.valideMessage;

public class StrategiesFournisseur {

    public static void strategieOffreMessage(AgentFournisseur agent, OfferMessage offerMessage, Historique historique) {
   
        valideMessage message = new valideMessage(agent, offerMessage.getSender(), offerMessage.getOffer());
        placePublic.getInstance(null).removeService( message.getOffer().getService() );
        agent.sendMessage(message);
  
    }

}
