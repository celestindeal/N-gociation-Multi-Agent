package Agent;

import Communication.*;
import Strategies.StrategiesNegociateur;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Classe pour les agents négociateurs
public class AgentNegociateur extends Agent {

    private Double money = 0.0;
    private Map<Agent, Historique> historique = new HashMap<>();

    public AgentNegociateur(int agentID, List<Preference> preferences, List<Contrainte> contraintes, Double money) {
        super(agentID, "Negociateur", preferences, contraintes);
        this.money = money;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(2000);
                if (historique.isEmpty() && !PlacePublique.getInstance().getServiceAvendre().isEmpty()) {
                    rechercheService();
                }
                if (!boiteAuxLettres.isEmpty()) {
                    traiterMessages(boiteAuxLettres.get(0));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private void traiterMessages(Message messageOfFournisseur) {
        this.log("Je traite un message du fournisseur " + messageOfFournisseur.getSender().getAgentID());
        if (messageOfFournisseur instanceof MessageOffre) {
            addHistorique(messageOfFournisseur.getSender(), null, messageOfFournisseur.getOffer());
            Message newMessage = StrategiesNegociateur.strategieBaisse((MessageOffre) messageOfFournisseur, historique.get(messageOfFournisseur.getSender()));

            sendMessage(newMessage);

            // mettre à jour l'historique de l'agent en fonction du message
            if (newMessage instanceof MessageOffre) {
                addHistorique(newMessage.getReceiver(), newMessage.getOffer(), null);
            } else {
                historique.remove(messageOfFournisseur.getSender());
            }


        } else if (messageOfFournisseur instanceof MessageRefus) {
            this.log("L'offre pour le service" + messageOfFournisseur.getOffer().getService().getServiceID() + "que j'ai envoyé a " + messageOfFournisseur.getSender().getAgentID() + " a été refusé");

            //supprimer l'hisorique de l'agent
            historique.remove(messageOfFournisseur.getSender());

        } else if (messageOfFournisseur instanceof MessageValide) {
            this.log("L'offre pour le service" + messageOfFournisseur.getOffer().getService().getServiceID() + "que j'ai envoyé a " + messageOfFournisseur.getSender().getAgentID() + " a été accepté");

            historique.remove(messageOfFournisseur.getSender());
        } else {
            // Message non reconnu
            System.out.println("Message non reconnu : " + messageOfFournisseur.toString());
        }

        // supprimer le message de la boite aux lettres
        boiteAuxLettres.remove(messageOfFournisseur);
    }

    private void addHistorique(Agent fournisseur, Offre offreNegociateur, Offre offreFournisseur) {
        if (offreNegociateur != null) historique.get(fournisseur).getOffreNegociateur().add(offreNegociateur);
        if (offreFournisseur != null) historique.get(fournisseur).getOffrefournisseur().add(offreFournisseur);
    }


    public List<Preference> getPreferencesUtilisateur() {
        return preferences;
    }

    public void setPreferencesUtilisateur(List<Preference> preferencesUtilisateur) {
        this.preferences = preferencesUtilisateur;
    }

    public List<Contrainte> getContraintesUtilisateur() {
        return contraintes;
    }

    public void setContraintesUtilisateur(List<Contrainte> contraintesUtilisateur) {
        this.contraintes = contraintesUtilisateur;
    }

    // fonction pour recherche un service dans la liste des services
    public void rechercheService() {
        // prend un service aléatoire dans la liste des services et envoyer une offre au prix
        this.log("Je recherche un service - Il y a " + PlacePublique.getInstance().getServiceAvendre().size() + " services en vente");
        Service service = PlacePublique.getInstance().getServiceAvendre().get((int) (Math.random() * PlacePublique.getInstance().getServiceAvendre().size()));

        // ajouter à l'histoirique
        Historique h = new Historique(service, service.getPrix() * 0.8);
        historique.put(service.getAgentFournisseur(), h);

        MessageOffre messegaToFournisseur = StrategiesNegociateur.strategieInitiale(this, service.getAgentFournisseur(), h);
        sendMessage(messegaToFournisseur);

    }

    private void valideTransaction() {

    }


}

