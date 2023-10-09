package Strate;

import Agent.AgentFournisseur;
import Agent.Historique;
import communication.OfferMessage;

public class StrategiesFournisseur {

    // retourne la nouvelle proposition 
    // si la valeur est la même c'est que le fournisseur accepte l'offre
    public static Double strategieOffreMessage(AgentFournisseur agent, OfferMessage offerMessage, Historique historique) {
   
      
        return offerMessage.getOffer().getPrix(); 
  
    }



}
