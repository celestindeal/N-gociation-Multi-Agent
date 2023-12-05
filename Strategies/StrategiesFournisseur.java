package Strategies;

import Agent.AgentFournisseur;
import Agent.Historique;
import Communication.MessageOffre;

public class StrategiesFournisseur {

    // retourne la nouvelle proposition 
    // si la valeur est la même c'est que le fournisseur accepte l'offre
    public static Double strategieOffreMessage(AgentFournisseur agent, MessageOffre offerMessage, Historique historique) {

        double newPriceOffer = (offerMessage.getOffer().getPrix() + historique.getTargetPrice()) / 2;
        return newPriceOffer;

    }
}
