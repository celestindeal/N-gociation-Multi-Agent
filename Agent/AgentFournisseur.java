package Agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import Strate.StrategiesFournisseur;
import communication.Message;
import communication.OfferMessage;
import communication.Offre;
import communication.placePublic;
import communication.refuseMessage;
import communication.valideMessage;

public class AgentFournisseur extends Agent {

    private Map<Agent, Historique> historique = new HashMap<>();

    public AgentFournisseur(int agentID, List<Preference> preferences, List<Contrainte> contraintes) {
        super(agentID, "Fournisseur", preferences, contraintes);
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (placePublic.getInstance(null).getserviceVenduFournisseur(this).isEmpty()) {
                    proposerService();
                }
                if(!boiteAuxLettres.isEmpty()) {
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
        ArrayList<Service> services = placePublic.getInstance(null).getServicesPossible();
        int serviceID = (int) (Math.random() * services.size());

        Random random = new Random();
        double valeurAleatoire = random.nextInt(800) + 200;

        Service service = new Service(serviceID, this, valeurAleatoire);    // créer un nouveau service

        // proposer le service sur la place public
        placePublic.getInstance(null).buyService(service);
    }

    

    // Fonction de gestion des messages
    public void traiterMessages(Message message) {
        System.out.println("FOURNISEUR " + this.agentID + " : Je traite un message du négociateur " + message.getSender().getAgentID());

        // contrôler si l'agent est autorisé à négocier le service
        if(!autoriserNegociationThisService(message.getSender(), message.getOffer().getService())) {
            System.out.println("FOURNISEUR " + this.agentID + " : l'agent " + message.getSender().getAgentID() + " n'est pas autorisé à négocier le service " + message.getOffer().getService().getServiceID());
            // envoyer un message de refus
            this.sendMessage(new refuseMessage(this, message.getSender(), message.getOffer().getService()));
            return;
        }

        if (message instanceof OfferMessage) {

            // réaliser la stratégie de l'offre et revoyer un message
            Double nouvelleProposition = StrategiesFournisseur.strategieOffreMessage(this, (OfferMessage) message, historique.get(message.getSender()));
            // si la valeur est la même c'est que le fournisseur accepte l'offre
            if( nouvelleProposition == message.getOffer().getPrix() ){
                accepteOffer((OfferMessage) message);
            }else if(0.0 < nouvelleProposition && nouvelleProposition < message.getOffer().getPrix()) {
                // envoyer un message de contre offre
                Offre nouvelleOffre = new Offre(message.getOffer().getService(), nouvelleProposition);
                this.sendMessage(new OfferMessage(this,  message.getSender(), nouvelleOffre));
                addHistorique(message.getSender(), message.getOffer(), nouvelleOffre);
            }else{
                // envoyer un message de refus
                this.sendMessage(new refuseMessage(this, message.getSender(), message.getOffer().getService()));
            }

        } else if (message instanceof refuseMessage) {
            System.out.println("FOURNISEUR " + this.agentID + " : je refuse l'offre de " + message.getSender().getAgentID() + " pour le service " + message.getOffer().getService().getServiceID() + " à " + message.getOffer().getPrix() + "€");
            
            //supprimer l'hisorique de l'agent
            historique.remove(message.getSender());

        } else if (message instanceof valideMessage) {
            System.out.println("Fournisseur " + this.agentID + " : l'offre de " + message.getSender().getAgentID() + " a été acceptée");

            //supprimer l'hisorique de l'agent et le service de la place public
            historique.remove(message.getSender());

            placePublic.getInstance(null).removeService( message.getOffer().getService() );

        } else {
            // Message non reconnu
            System.out.println("Message non reconnu : " + message.toString());
        }
        

        // Effacer les messages traités de la boîte aux lettres
        boiteAuxLettres.remove(message);
    }


    private void addHistorique(Agent sender, Offre offerNegociateur, Offre offerFournisseur) {
        // si l'agent n'a pas encore d'historique on le créer
        if(!historique.containsKey(sender)) {
            Historique h = new Historique();
            h.addOffreNegociateur(offerNegociateur);
            h.addOffreFournisseur(offerFournisseur);
            historique.put(sender, h);
        }else{
            // sinon on ajoute l'offre à l'historique
            historique.get(sender).addOffreNegociateur(offerNegociateur);
            historique.get(sender).addOffreFournisseur(offerFournisseur);
        }
    }

    private void accepteOffer(OfferMessage offerMessage) {
        System.out.println("FOURNISEUR " + this.agentID + " : j'accepte l'offre de " + offerMessage.getSender().getAgentID() + " pour le service " + offerMessage.getOffer().getService().getServiceID() + " à " + offerMessage.getOffer().getPrix());

        // envoyer des messages de refus aux autres agents négociateurs qui négoce le même service et suppression de leurs historique et de leurs messages en attente
        for(Agent agentKey : historique.keySet()){
            Historique historiqueValue = historique.get(agentKey);
            // si c'est le même service qui est négocier
            if(historiqueValue.getOffreNegociateur().get(0).getService().equals(offerMessage.getOffer().getService()) && !agentKey.equals(offerMessage.getSender())){
                System.out.println("FOURNISEUR " + this.agentID + " : je refuse l'offre de " + agentKey.getAgentID() + " pour le service " + historiqueValue.getOffreNegociateur().get(0).getService().getServiceID() + " car je l'ai vendu à " + offerMessage.getSender().getAgentID());
                this.sendMessage(new refuseMessage(this, agentKey, historiqueValue.getOffreNegociateur().get(0).getService()));
                // supprimer l'historique de l'agent
                historique.remove(agentKey);
                // supprimer le message de la boite aux lettres
                boiteAuxLettres.remove(historiqueValue.getOffreNegociateur().get(0));
            }
        }


        // envoyer des messages de refus aux autres agents négociateurs qui on envoyer une offre pour le même service et suppression de leurs messages en attente

        List<Message> testboiteAuxLettres  = new CopyOnWriteArrayList<>(this.boiteAuxLettres);
        for(Message m : testboiteAuxLettres) {
            if(!m.equals(offerMessage) && m instanceof OfferMessage && m.getOffer().getService().equals(offerMessage.getOffer().getService()) ) {
                System.out.println("FOURNISEUR " + this.agentID + " : je refuse l'offre de " + m.getSender().getAgentID() + " pour le service " + m.getOffer().getService().getServiceID() + " car je l'ai vendu à " + offerMessage.getSender().getAgentID());
                this.sendMessage(new refuseMessage(this, m.getSender(), m.getOffer().getService()));
                // supprimer le message de la boite aux lettres
                boiteAuxLettres.remove(m);
            }
        }


        // envoyer un message de validation
        valideMessage message = new valideMessage(this, offerMessage.getSender(), offerMessage.getOffer());
        sendMessage(message);

        //supprimer le service vendu de la place public
        placePublic.getInstance(null).removeService( message.getOffer().getService() );
        // supprimer l'historique de cette négociation
        historique.remove(offerMessage.getSender());
    
    }

    //  on autorise la negociation d'un service avec un seul agent à la fois
    private boolean autoriserNegociationThisService(Agent agent, Service service) {

        // si l'agent négocie déjà une autre offret on refuse la negociation
        if(historique.containsKey(agent) && !historique.get(agent).getOffreNegociateur().get(0).getService().equals(service)) {
            // l'agent a déjà negocier ce service
            return false;
        }

        //Savoir si le service est déjà negocier 
        for(Historique h : historique.values()) {
            if(h.getOffreNegociateur().get(0).getService().equals(service)) {
                // le service est déjà negocier
                // si c'est parle le même agent on autorise la negociation
                if(h.getOffreNegociateur().get(0).equals(agent)) {
                    return true;
                } else {
                    // sinon on refuse la negociation
                    return false;
                }
            }
        }
        // le service n'est pas encore negocier
        return true;
        
    }
}
