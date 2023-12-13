package Agent;

import Communication.*;
import Strategies.StrategiesFournisseur;

import java.util.*;

public class AgentFournisseur extends Agent {
    private Map<Agent, Historique> historique = new HashMap<>();

    public AgentFournisseur(int agentID, List<Preference> preferences, List<Contrainte> contraintes) {
        super(agentID, "Fournisseur", preferences, contraintes);
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (PlacePublique.getInstance().getServiceVenduFournisseur(this).isEmpty()) {
                    proposerService();
                }
                if (!boiteAuxLettres.isEmpty()) {
                    traiterOffre();
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    // Fonction de gestion des messages
    public void traiterOffre() {
        ArrayList<Service> servicesEnVente = PlacePublique.getInstance().getServiceAvendre();

        cleanBoiteAuxLettres();

        ArrayList<Message> messagesEnvoie = new ArrayList<>();
        // On parcourt les services
        for (Service service : servicesEnVente) {
            // trouver toutes les offres pour ce service
            ArrayList<Message> offres = new ArrayList<>();
            // Dans le service : on récupère tous les messages pour le service
            Iterator<Message> iterator = boiteAuxLettres.iterator();
            while (iterator.hasNext()) {
                Message message = iterator.next();
                if (message.getOffer().getService().equals(service)) {
                    offres.add(message);
                    addHistorique(message.getSender(), message.getOffer(), null);
                    iterator.remove();
                }
            }
            // On applique la stratégie et on récupère tous les messages à envoyer
            messagesEnvoie.addAll(StrategiesFournisseur.strategieFournisseur(offres, historique));
        }

        // On envoie les messages
        for (Message message : messagesEnvoie) {
            sendMessage(message);
            if (message instanceof MessageValide) {
                // supprimer l'historique de l'agent qui a envoyé l'offre
                historique.remove(message.getReceiver());
                // supprimer les services de la vente (place publique)
                PlacePublique.getInstance().removeService(message.getOffer().getService());
            } else if (message instanceof MessageRefus) {
                // supprimer l'historique de l'agent qui a envoyé l'offre
                historique.remove(message.getReceiver());
            }
        }
    }

    private void cleanBoiteAuxLettres() {
        ArrayList<Message> messagesASupprimer = new ArrayList<>();
        for (Message message : boiteAuxLettres) {
            if (!(message instanceof MessageOffre)) {
                cleanMessage(message, (AgentNegociateur) message.getSender());
                //supprimer le message que l'on viens de traiter de la boite aux lettres
                messagesASupprimer.add(message);
            }
        }
        boiteAuxLettres.removeAll(messagesASupprimer);
    }

    private void cleanMessage(Message message, AgentNegociateur agent) {
        if (message instanceof MessageValide) {
            accepteOffer((MessageValide) message);
            // supprimer l'historique de l'agent qui a envoyé l'offre
            agent.getServicesRefuses().add(message.getOffer().getService());
            historique.remove(agent);
        } else if (message instanceof MessageRefus) {
            // supprimer l'historique de l'agent qui a envoyé l'offre
            historique.remove(agent);
        }
    }

    private void proposerService() {
        // Choisir un service aléatoirement
        ArrayList<Service> services = PlacePublique.getInstance().getServicesPossible();
        int serviceID = (int) (Math.random() * services.size());

        Random random = new Random();
        double valeurAleatoire = random.nextInt(800) + 200;

        Service service = new Service(serviceID, this, valeurAleatoire);    // créer un nouveau service

        // proposer le service sur la place public
        PlacePublique.getInstance().buyService(service);
    }


    private void addHistorique(Agent negociateur, Offre offreNegociateur, Offre offreFournisseur) {
        // si l'agent n'a pas encore d'historique on le créer
        if (!historique.containsKey(negociateur)) {
            // On génère une variable entre -10% et -20% du prix du service
            double targetPrice = offreNegociateur.getService().getPrix() * (0.8 + Math.random() * 0.1);
            Historique h = new Historique(offreNegociateur.getService(), targetPrice);
            log("Le prix cible est de " + targetPrice);
            if (offreNegociateur != null) h.getOffreNegociateur().add(offreNegociateur);
            if (offreFournisseur != null) h.getOffrefournisseur().add(offreFournisseur);
            historique.put(negociateur, h);
        } else {
            // sinon on ajoute l'offre à l'historique
            if (offreNegociateur != null) historique.get(negociateur).getOffreNegociateur().add(offreNegociateur);
            if (offreFournisseur != null) historique.get(negociateur).getOffrefournisseur().add(offreFournisseur);
        }
    }

    private void accepteOffer(MessageValide offerMessage) {
        // On accepte l'offre
        sendMessage(new MessageValide(offerMessage.getReceiver(), offerMessage.getSender(), offerMessage.getOffer()));
        // On retire le service de la vente
        PlacePublique.getInstance().removeService(offerMessage.getOffer().getService());
        // On supprime l'historique de l'agent
        historique.remove(offerMessage.getSender());
    }

    //  on autorise la negociation d'un service avec un seul agent à la fois
    private boolean autoriserNegociationThisService(Agent agent, Service service) {
        // si l'agent négocie déjà une autre offret on refuse la negociation
        if (historique.containsKey(agent) && !historique.get(agent).getOffreNegociateur().get(0).getService().equals(service)) {
            // l'agent a déjà negocier ce service
            this.log("Je refuse la négociation : l'agent est déjà sur une autre offre");
            return false;
        }

        //Savoir si le service est déjà negocier 
        for (Agent agentIter : historique.keySet()) {
            Historique h = historique.get(agentIter);
            if (h.getOffreNegociateur().get(0).getService().equals(service)) {
                // le service est déjà negocier
                // si c'est parle le même agent on autorise la negociation
                if (agentIter.equals(agent)) {
                    return true;
                } else {
                    // sinon on refuse la negociation
                    this.log("Je refuse la négociation : le service est déjà négocié");
                    return false;
                }
            }
        }
        // le service n'est pas encore negocier
        return true;

    }
}
