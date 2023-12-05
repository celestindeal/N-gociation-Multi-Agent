package Agent;

import Communication.*;
import Strategies.StrategiesFournisseur;

import javax.sound.midi.Soundbank;
import java.sql.SQLOutput;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class AgentFournisseur extends Agent {

    private Map<Agent, Historique> historique = new HashMap<>();

    public AgentFournisseur(int agentID, List<Preference> preferences, List<Contrainte> contraintes) {
        super(agentID, "Fournisseur", preferences, contraintes);
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (PlacePublique.getInstance().getserviceVenduFournisseur(this).isEmpty()) {
                    proposerService();
                }
                if (!boiteAuxLettres.isEmpty()) {
                    traiterMessages(boiteAuxLettres.get(0));
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
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


    // Fonction de gestion des messages
    public void traiterMessages(Message message) {
        this.log("Je traite un message du négociateur " + message.getSender().getAgentID());

        // contrôler si l'agent est autorisé à négocier le service
        if (!this.autoriserNegociationThisService(message.getSender(), message.getOffer().getService())) {
            this.log("L'agent " + message.getSender().getAgentID() + " n'est pas autorisé à négocier le service " + message.getOffer().getService().getServiceID());
            // envoyer un message de refus
            this.sendMessage(new MessageRefus(this, message.getSender(), message.getOffer().getService()));
            // Effacer les messages traités de la boîte aux lettres
            boiteAuxLettres.remove(message);
            return;
        }

        if (message instanceof MessageOffre) {
            // Initialisation : on regarde s'il y a un historique (première offre)
            if (historique.get(message.getSender()) == null) {
                addHistorique(message.getSender(), message.getOffer(), null);
            }
            Double nouvelleProposition = StrategiesFournisseur.strategieOffreMessage(this, (MessageOffre) message, historique.get(message.getSender()));
            Offre nouvelleOffre = new Offre(message.getOffer().getService(), nouvelleProposition);
            this.sendMessage(new MessageOffre(this, message.getSender(), nouvelleOffre));
            this.addHistorique(message.getSender(), message.getOffer(), nouvelleOffre);

        } else if (message instanceof MessageRefus) {
            this.log("Je refuse l'offre de " + message.getSender().getAgentID() + " pour le service " + message.getOffer().getService().getServiceID() + " à " + message.getOffer().getPrix() + "€");

            //supprimer l'hisorique de l'agent
            historique.remove(message.getSender());

        } else if (message instanceof MessageValide) {
            this.log("J'accepte l'offre de " + message.getSender().getAgentID() + " pour le service " + message.getOffer().getService().getServiceID() + " à " + message.getOffer().getPrix() + "€");

            //supprimer l'hisorique de l'agent et le service de la place public
            historique.remove(message.getSender());

            PlacePublique.getInstance().removeService(message.getOffer().getService());

        } else {
            // Message non reconnu
            System.out.println("Message non reconnu : " + message.toString());
        }

        // Effacer les messages traités de la boîte aux lettres
         boiteAuxLettres.remove(message);
    }


    private void addHistorique(Agent negociateur, Offre offreNegociateur, Offre offreFournisseur) {
        // si l'agent n'a pas encore d'historique on le créer
        if (!historique.containsKey(negociateur)) {
            Historique h = new Historique(offreNegociateur.getService(), offreNegociateur.getService().getPrix()*0.8);
            if(offreNegociateur != null) h.getOffreNegociateur().add(offreNegociateur);
            if(offreFournisseur != null) h.getOffrefournisseur().add(offreFournisseur);
            historique.put(negociateur, h);
        } else {
            // sinon on ajoute l'offre à l'historique
            if(offreNegociateur != null) historique.get(negociateur).getOffreNegociateur().add(offreNegociateur);
            if(offreFournisseur != null) historique.get(negociateur).getOffrefournisseur().add(offreFournisseur);
        }
    }

    private void accepteOffer(MessageOffre offerMessage) {
        this.log("J'accepte l'offre de " + offerMessage.getSender().getAgentID() + " pour le service " + offerMessage.getOffer().getService().getServiceID() + " à " + offerMessage.getOffer().getPrix());

        // envoyer des messages de refus aux autres agents négociateurs qui négoce le même service et suppression de leurs historique et de leurs messages en attente
        for (Agent agentKey : historique.keySet()) {
            Historique historiqueValue = historique.get(agentKey);
            // si c'est le même service qui est négocier
            if (historiqueValue.getOffreNegociateur().get(0).getService().equals(offerMessage.getOffer().getService()) && !agentKey.equals(offerMessage.getSender())) {
                this.log("Je refuse l'offre de " + agentKey.getAgentID() + " pour le service " + historiqueValue.getOffreNegociateur().get(0).getService().getServiceID() + " car je l'ai vendu à " + offerMessage.getSender().getAgentID());
                this.sendMessage(new MessageRefus(this, agentKey, historiqueValue.getOffreNegociateur().get(0).getService()));
                // supprimer l'historique de l'agent
                historique.remove(agentKey);
                // supprimer le message de la boite aux lettres
                boiteAuxLettres.remove(historiqueValue.getOffreNegociateur().get(0));
            }
        }


        // envoyer des messages de refus aux autres agents négociateurs qui on envoyer une offre pour le même service et suppression de leurs messages en attente

        List<Message> testboiteAuxLettres = new CopyOnWriteArrayList<>(this.boiteAuxLettres);
        for (Message m : testboiteAuxLettres) {
            if (!m.equals(offerMessage) && m instanceof MessageOffre && m.getOffer().getService().equals(offerMessage.getOffer().getService())) {
                this.log("Je refuse l'offre de " + m.getSender().getAgentID() + " pour le service " + m.getOffer().getService().getServiceID() + " car je l'ai vendu à " + offerMessage.getSender().getAgentID());
                this.sendMessage(new MessageRefus(this, m.getSender(), m.getOffer().getService()));
                // supprimer le message de la boite aux lettres
                boiteAuxLettres.remove(m);
            }
        }


        // envoyer un message de validation
        MessageValide message = new MessageValide(this, offerMessage.getSender(), offerMessage.getOffer());
        sendMessage(message);

        //supprimer le service vendu de la place public
        PlacePublique.getInstance().removeService(message.getOffer().getService());
        // supprimer l'historique de cette négociation
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
        for (Historique h : historique.values()) {
            if (h.getOffreNegociateur().get(0).getService().equals(service)) {
                // le service est déjà negocier
                // si c'est parle le même agent on autorise la negociation
                if (h.getOffreNegociateur().get(0).equals(agent)) {
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
