package Strate;

import Agent.AgentFournisseur;
import Agent.AgentNegociateur;
import Agent.Historique;
import communication.OfferMessage;
import communication.valideMessage;

public class StrategiesNegociateur {

    public static void strategieOffreMessage(AgentNegociateur agent, OfferMessage offerMessage, Historique historique) {
   
        valideMessage message = new valideMessage(agent, offerMessage.getSender(), offerMessage.getOffer());
        agent.sendMessage(message);
  
    }

}
