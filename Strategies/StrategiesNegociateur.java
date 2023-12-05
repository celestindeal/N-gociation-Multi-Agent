package Strategies;

import Agent.AgentFournisseur;
import Agent.AgentNegociateur;
import Agent.Historique;
import Agent.Service;
import Communication.*;

public class StrategiesNegociateur {
    public static MessageOffre strategieInitiale(AgentNegociateur moi, AgentFournisseur cible, Historique historique) {
        return new MessageOffre(moi, cible, new Offre(historique.getService(), historique.getTargetPrice() * 0.8));
    }


    public static Message strategieBaisse(MessageOffre messageOffre, Historique historique) {
        // Dans le cas où le prix est inférieur à la cible, on accepte l'offre
        if (messageOffre.getOffer().getPrix() <= historique.getTargetPrice()) {
            return new MessageValide(messageOffre.getReceiver(), messageOffre.getSender(), messageOffre.getOffer());
        }

        // Si l'offre est supérieure de 30% à la cible, on refuse l'offre
        if (messageOffre.getOffer().getPrix() > historique.getTargetPrice() * 1.3) {
            return new MessageRefus(messageOffre.getReceiver(), messageOffre.getSender(), historique.getService());
        }

        if (historique.getOffrefournisseur().size() < 3) {
            // Si on commence les négociations, on propose un prix inférieur à la cible agressivement
            return new MessageOffre(messageOffre.getReceiver(), messageOffre.getSender(), new Offre(historique.getService(), strategieAggressive(messageOffre.getOffer().getPrix(), historique.getTargetPrice())));
        } else if (historique.getOffrefournisseur().size() < 10) {
            // Si on est au coeur des négociations, on propose un prix inférieur à la cible normalement
            return new MessageOffre(messageOffre.getReceiver(), messageOffre.getSender(), new Offre(historique.getService(), strategieMoyenne(messageOffre.getOffer().getPrix(), historique.getTargetPrice())));
        } else if (historique.getOffrefournisseur().size() < 20) {
            // Si on est à la fin des négociations, on propose un prix inférieur à la cible timidement
            return new MessageOffre(messageOffre.getReceiver(), messageOffre.getSender(), new Offre(historique.getService(), strategieTimide(messageOffre.getOffer().getPrix(), historique.getTargetPrice())));
        }

        return new MessageRefus(messageOffre.getReceiver(), messageOffre.getSender(), historique.getService());
    }

    private static double strategieMoyenne(double prix, double targetPrice) {
        return (prix + targetPrice) / 2;
    }

    private static double strategieTimide(double prix, double targetPrice) {
        return prix - (prix - targetPrice) * 0.05;
    }

    private static double strategieAggressive(double prix, double targetPrice) {
        return prix - (prix - targetPrice) * 0.2;
    }

}
