package Strategies;

import Agent.AgentFournisseur;
import Agent.AgentNegociateur;
import Agent.Historique;
import Communication.*;

public class StrategiesNegociateur {
    public static MessageOffre strategieInitiale(AgentNegociateur moi, AgentFournisseur cible, Historique historique) {
        double prixService = historique.getService().getPrix();
        double prixTarget = historique.getTargetPrice();
        double prixAgressif = strategieNormale(prixService, prixTarget);
        return new MessageOffre(moi, cible, new Offre(historique.getService(), prixAgressif * (.8 + Math.random() * .1)));
    }


    public static Message strategieBaisse(MessageOffre messageOffre, Historique historique) {
        // Dans le cas où le prix est inférieur à la cible, on accepte l'offre
        if (messageOffre.getOffer().getPrix() <= historique.getTargetPrice()) {
            return new MessageValide(messageOffre.getReceiver(), messageOffre.getSender(), messageOffre.getOffer());
        }

        // Si l'offre est supérieure de 30% à la cible, on refuse l'offre
        if (messageOffre.getOffer().getPrix() > historique.getTargetPrice() * 1.3 || !historique.getTimeValide()) {
            return new MessageRefus(messageOffre.getReceiver(), messageOffre.getSender(), historique.getService());
        }

        if (historique.getOffrefournisseur().size() < 2) {
            // Si on commence les négociations, on propose un prix inférieur à la cible agressivement
            return new MessageOffre(messageOffre.getReceiver(), messageOffre.getSender(), new Offre(historique.getService(), strategieAggressive(messageOffre.getOffer().getPrix(), historique.getTargetPrice())));
        } else if (historique.getOffrefournisseur().size() < 4) {
            // Si on est au coeur des négociations, on propose un prix inférieur à la cible normalement
            return new MessageOffre(messageOffre.getReceiver(), messageOffre.getSender(), new Offre(historique.getService(), strategieNormale(messageOffre.getOffer().getPrix(), historique.getTargetPrice())));
        } else if (historique.getOffrefournisseur().size() < 6) {
            // Si on est à la fin des négociations, on propose un prix inférieur à la cible timidement
            return new MessageOffre(messageOffre.getReceiver(), messageOffre.getSender(), new Offre(historique.getService(), strategieTimide(messageOffre.getOffer().getPrix(), historique.getTargetPrice())));
        }

        return new MessageRefus(messageOffre.getReceiver(), messageOffre.getSender(), historique.getService());
    }

    private static double strategieAggressive(double prixLastOffre, double prixTarget) {
        double dif = prixLastOffre - prixTarget;
        return prixLastOffre - (dif * 2);
    }

    private static double strategieNormale(double prixLastOffre, double prixTarget) {
        double dif = prixLastOffre - prixTarget;
        return prixLastOffre - (dif * 1.5);
    }

    private static double strategieTimide(double prixLastOffre, double prixTarget) {
        return prixTarget;
    }

}
