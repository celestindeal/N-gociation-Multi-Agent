package Agent;

import Communication.*;
import Strategies.StrategiesNegociateur;

import java.util.*;

// Classe pour les agents négociateurs
public class AgentNegociateur extends Agent {
    private Map<Agent, Historique> historique = new HashMap<>();
    private ArrayList<Service> servicesRefuses = new ArrayList<>();
    private ArrayList<Service> servicesEnNegoce = new ArrayList<>();

    public AgentNegociateur(int agentID, List<Preference> preferences, List<Contrainte> contraintes, Double money) {
        super(agentID, "Negociateur", preferences, contraintes);
    }

    public ArrayList<Service> getServicesRefuses() {
        return servicesRefuses;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(2000);
                if (!boiteAuxLettres.isEmpty()) {
                    traiterMessages(boiteAuxLettres.get(0));
                }
                if (!PlacePublique.getInstance().getServiceAvendre().isEmpty()) {
                    rechercheService();
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
            this.log("L'offre pour le service " + messageOfFournisseur.getOffer().getService().getServiceID() + " que j'ai envoyé a " + messageOfFournisseur.getSender().getAgentID() + " a été refusé");

            // supprimer l'historique de l'agent
            historique.remove(messageOfFournisseur.getSender());
            // ajouter le service à la liste des services refusés
            servicesRefuses.add(messageOfFournisseur.getOffer().getService());

        } else if (messageOfFournisseur instanceof MessageValide) {
            this.log("L'offre pour le service " + messageOfFournisseur.getOffer().getService().getServiceID() + " que j'ai envoyé a " + messageOfFournisseur.getSender().getAgentID() + " a été accepté");

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
        // Parmi la liste des services disponibles, je vais faire une offre sur tous les services qui ne sont pas dans la liste des services refusés
        this.log("Je recherche un service - Il y a " + PlacePublique.getInstance().getServiceAvendre().size() + " services en vente");
        boolean serviceTrouve = false;
        for (Service service : PlacePublique.getInstance().getServiceAvendre()) {
            if (!servicesRefuses.contains(service) && !servicesEnNegoce.contains(service)) {
                serviceTrouve = true;
                // ajouter à l'historique
                Random random = new Random();
                int nombreMaxMessage = random.nextInt(8) + 3;
                double randomNumber = (0.8 + Math.random() * 0.1);

                Historique h = new Historique(service, service.getPrix() * randomNumber, nombreMaxMessage);
                log("Le prix cible est de " + service.getPrix() * randomNumber);
                historique.put(service.getAgentFournisseur(), h);

                MessageOffre messageToFournisseur = StrategiesNegociateur.strategieInitiale(this, service.getAgentFournisseur(), h);
                sendMessage(messageToFournisseur);
                // ajouter le service à la liste des services en négociation
                servicesEnNegoce.add(service);
            }
        }
        if (!serviceTrouve) {
            this.log("Je n'ai pas trouvé de service à acheter");
        }
    }

    private void valideTransaction() {

    }


}

