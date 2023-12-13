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

    /**
     * Stratégie qui choisit l'offre la plus basse et refuse toutes les autres
     * Si la stratégie la plus basse est dans le seuil, on accepte l'offre
     * Sinon, on lance une nouvelle négociation
     *
     * @param offres
     * @param historiqueMap
     * @return
     */
    private static ArrayList<Message> strategiePlusBasse(ArrayList<Message> offres, Map<Agent, Historique> historiqueMap) {
        if (offres.isEmpty()) {
            return new ArrayList<>();
        }

        // On parcourt toutes les offres pour trouver la plus basse
        Message offreLaPlusHaute = offres.get(0);
        for (Message offre : offres) {
            log("Offre pour le service " + offre.getOffer().getService().getServiceID() + " au prix de " + offre.getOffer().getPrix() + "€", offre.getReceiver());
            if (offre.getOffer().getPrix() > offreLaPlusHaute.getOffer().getPrix()) {
                offreLaPlusHaute = offre;
            } else if (offre.getOffer().getPrix() == offreLaPlusHaute.getOffer().getPrix()) {
                // Si deux offres ont le même prix, on choisit aléatoirement
                if (Math.random() < 0.5) {
                    offreLaPlusHaute = offre;
                }
            }
        }
        log("L'offre la plus haute est de " + offreLaPlusHaute.getOffer().getPrix() + "€", offreLaPlusHaute.getReceiver());

        // On envoie une offre à tous les agents qui ont fait une offre
        ArrayList<Message> messages = new ArrayList<>();

        for (Message offre : offres) {
            // On traite l'offre la plus basse différemment
            Agent negociateur = offre.getSender();
            Agent fournisseur = offre.getReceiver();
            Historique historique = historiqueMap.get(negociateur);
            double prixOffre = offre.getOffer().getPrix();
            double prixTarget = historique.getTargetPrice();

            if (offre.equals(offreLaPlusHaute)) {
                // Si la différence entre l'offre et le prix du fournisseur est dans le seuil, on accepte l'offre
                if (prixOffre >= prixTarget) {
                    log("L'offre à " + offre.getOffer().getPrix() + "€ est dans le seuil, j'accepte l'offre", fournisseur);
                    messages.add(new MessageValide(fournisseur, negociateur, offre.getOffer()));
                } else {
                    // Sinon, on lance une nouvelle négociation
                    log("L'offre à " + offre.getOffer().getPrix() + "€ n'est pas dans le seuil, je lance une nouvelle négociation", fournisseur);
                    messages.add(new MessageOffre(fournisseur, negociateur, new Offre(historique.getService(), negocePrix(prixOffre, prixTarget))));
                }
            } else {
                // On refuse toutes les autres offres
                log("Je refuse l'offre à " + offre.getOffer().getPrix() + "€ car elle est plus basse que l'offre la plus haute", fournisseur);
                messages.add(new MessageRefus(fournisseur, negociateur, historique.getService()));
            }
        }

        return messages;
    }

    private static void log(String message, Agent fournisseur) {
        System.out.println("[Fournisseur " + fournisseur.getAgentID() + "] [Stratégie]\t" + message);
    }

    private static double negocePrix(double prixOffre, double prixTarget) {
        // On choisit une stratégie de négociation en fonction de la différence entre le prix de l'offre et le prix cible
        double pourcentage = Math.abs((prixOffre - prixTarget) / prixTarget * 100);
        System.out.print("[Stratégie F]\t\t\t\tLa différence entre le prix de l'offre et le prix cible est de " + pourcentage + "% - ");
        if (pourcentage > 20) {
            return negoceAggressive(prixOffre, prixTarget);
        } else if (pourcentage > 10) {
            return negoceNormal(prixOffre, prixTarget);
        } else {
            return negocePassif(prixOffre, prixTarget);
        }
    }

    private static double negoceAggressive(double prixOffre, double prixTarget) {
        // Pour une négoce aggressive, on propose un prix inférieur de 2 fois la différence entre le prix de l'offre et le prix cible
        double dif = prixOffre - prixTarget;
        double pourcentage = Math.abs((prixOffre - prixTarget) / prixTarget * 100);
        System.out.println("On propose un prix de " + (prixOffre - (dif * 0.9)) + "€ (" + pourcentage + "%)");
        return prixOffre - (dif * 0.9);
    }

    private static double negoceNormal(double prixOffre, double prixTarget) {
        double dif = prixOffre - prixTarget;
        double pourcentage = Math.abs((prixOffre - prixTarget) / prixTarget * 100);
        System.out.println("On propose un prix de " + (prixOffre - (dif * 0.5)) + "€ (" + pourcentage + "%)");
        return prixOffre - (dif * 0.5);
    }

    private static double negocePassif(double prixOffre, double prixTarget) {
        double dif = prixOffre - prixTarget;
        double pourcentage = Math.abs((prixOffre - prixTarget) / prixTarget * 100);
        System.out.println("On propose un prix de " + (prixOffre - (dif * 0.2)) + "€ (" + pourcentage + "%)");
        return prixOffre - (dif * 0.2);
    }
}
