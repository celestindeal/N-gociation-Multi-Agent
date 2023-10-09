package Strate;

import Agent.AgentFournisseur;
import Agent.AgentNegociateur;
import Agent.Historique;
import communication.OfferMessage;
import communication.valideMessage;

public class StrategiesNegociateur {

    public static double  strategieOffreMessage(AgentNegociateur agent, OfferMessage offerMessage, Historique historique) {

        return offerMessage.getOffer().getPrix();
   

  
    }

}
