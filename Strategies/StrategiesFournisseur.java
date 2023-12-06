package Strategies;

import Agent.Agent;
import Agent.Historique;
import Communication.*;

import java.util.ArrayList;
import java.util.Map;

public class StrategiesFournisseur {
    public static ArrayList<Message> strategieFournisseur(ArrayList<Message> offres, Map<Agent, Historique> historiqueMap) {
        return strategiePlusBasse(offres, historiqueMap);
    }

    private static ArrayList<Message> strategiePlusBasse(ArrayList<Message> offres, Map<Agent, Historique> historiqueMap) {
        // On parcourt toutes les offres pour trouver la plus basse
        Message offreLaPlusBasse = offres.get(0);
        for (Message offre : offres) {
            if (offre.getOffer().getPrix() < offreLaPlusBasse.getOffer().getPrix()) {
                offreLaPlusBasse = offre;
            } else if (offre.getOffer().getPrix() == offreLaPlusBasse.getOffer().getPrix()) {
                // Si deux offres ont le même prix, on choisit aléatoirement
                if (Math.random() < 0.5) {
                    offreLaPlusBasse = offre;
                }
            }
        }

        // On envoie une offre à tous les agents qui ont fait une offre
        ArrayList<Message> messages = new ArrayList<>();

        for (Message offre : offres) {
            // On traite l'offre la plus basse différemment
            if (offre.equals(offreLaPlusBasse)) {
                // Si l'offre est dans le budget du fournisseur, on accepte
                if (offre.getOffer().getPrix() <= historiqueMap.get(offre.getReceiver()).getTargetPrice()) {
                    messages.add(new MessageValide(offre.getReceiver(), offre.getSender(), offre.getOffer()));
                } else {
                    // Sinon, on lance une nouvelle négociation
                    messages.add(
                            new MessageOffre(
                                    offre.getReceiver(),
                                    offre.getSender(),
                                    new Offre(
                                            historiqueMap.get(offre.getSender()).getService(),
                                            negocePrix(offre.getOffer().getPrix(), historiqueMap.get(offre.getReceiver()).getTargetPrice()
                                            )
                                    )
                            )
                    );
                }
            } else {
                // On refuse toutes les autres offres
                messages.add(new MessageRefus(offre.getReceiver(), offre.getSender(), historiqueMap.get(offre.getSender()).getService()));
            }
        }

        return messages;
    }


    private static double negocePrix(double prixOffre, double prixCible) {
        // On choisit une stratégie de négociation en fonction de la différence entre le prix de l'offre et le prix cible
        double difference = prixOffre - prixCible;
        if (difference > 100) {
            return negoceAggressif(prixOffre, prixCible);
        } else if (difference > 50) {
            return negocePassif(prixOffre, prixCible);
        } else {
            return negoceNormal(prixOffre, prixCible);
        }
    }

    private static double negoceAggressif(double prixOffre, double prixCible) {
        return prixOffre - (prixOffre - prixCible) * 0.1;
    }

    private static double negocePassif(double prixOffre, double prixCible) {
        return prixOffre - (prixOffre - prixCible) * 0.05;
    }

    private static double negoceNormal(double prixOffre, double prixCible) {
        return prixOffre - (prixOffre - prixCible) * 0.075;
    }
}
