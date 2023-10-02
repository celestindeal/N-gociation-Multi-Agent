package Agent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import communication.Message;
import communication.OfferMessage;
import communication.Offre;
import communication.placePublic;
import communication.refuseMessage;
import communication.valideMessage;

// Classe pour les agents fournisseurs
public class AgentFournisseur extends Agent {

    private ArrayList <Service> serviceQueJevend= null;  //  liste des services proposés et leurs prix minimum voulu
    private Map<Agent, Historique> historique = null;
    

    public AgentFournisseur(int agentID, List<Preference> preferences, List<Contrainte> contraintes) {
        super(agentID, "Fournisseur", preferences, contraintes);
        this.historique = new HashMap<>();
        this.serviceQueJevend = new ArrayList<>();
    }

    @Override
    public void run() {
        while (true) {
            try {
                if ( serviceQueJevend.isEmpty()) {
                  proposerService();
                }
                Thread.sleep(1000); 
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private void proposerService() {
        // Choisir un servie aléatoirement
        ArrayList<Service> services = placePublic.getInstance(null).getServicesPossible();
        int serviceID = (int) (Math.random() * services.size());

        Random random = new Random();
        double valeurAleatoire = random.nextInt(800) + 200;

        Service service = new Service(serviceID, this, valeurAleatoire);    // créer un nouveau service

        // proposer le service sur la place public 
        placePublic.getInstance(null).buyService(service);

        // ajouter le service dans la liste des services proposés
        serviceQueJevend.add(service);

        System.out.println("Fournisseur " + this.agentID + " : Je propose le service " + serviceID + " au prix de " + service.getPrix() + "€");
    }


    @Override
    public Message receiveMessage(Message message){
        // il y as trois type de message possible OffreMessage RequestMessage et AcceptMessage
        if (message instanceof OfferMessage) {
            placePublic.getInstance(null).removeService(((OfferMessage) message).getOffre().getService());  // supprimer le service de la place public
            serviceQueJevend.remove(((OfferMessage) message).getOffre().getService());  // supprimer le service de la liste des services proposés
            return new valideMessage(this, message.getSender(), "Je comprend votre message");
            // return strategieOffreMessage((OfferMessage) message);
        }
        return new refuseMessage(this, message.getSender(), "Je ne comprend pas votre message");
    }


    private Message strategieOffreMessage(OfferMessage offerMessage) {

        // si c'est la première offre de la part de l'agent négociateur
        if (!historique.containsKey(offerMessage.getSender())){
            // créer l'historique et on retourne une offre avec le même prix que l'offre original
            
            OfferMessage reponse = new OfferMessage(this, offerMessage.getSender(), offerMessage.getOffre().getService(), offerMessage.getOffre().getService().getPrix());
            

            ArrayList<Offre> Offrefournisseur = new ArrayList<Offre>();
            ArrayList<Offre> OffreNegociateur = new ArrayList<Offre>();
            Offrefournisseur.add(reponse.getOffre());
            OffreNegociateur.add(offerMessage.getOffre());

            Historique h = new Historique(Offrefournisseur, OffreNegociateur);
            historique.put(offerMessage.getSender(), h);

            return  reponse;
        }else{
            // On autorise que trois offre de la part des agents négociateurs
            if (historique.get(offerMessage.getSender()).getOffreNegociateur().size() < 3){
                /////////// on vas faire une contre offre ///////////////////////

                // on retourne une offre avec un prix plus bas que l'offre original
                OfferMessage reponse = new OfferMessage(this, offerMessage.getSender(), offerMessage.getOffre().getService(), offerMessage.getOffre().getService().getPrix() * 0.9);
                
                 // on ajoute l'offre dans l'historique
                historique.get(offerMessage.getSender()).getOffreNegociateur().add(offerMessage.getOffre());
                historique.get(offerMessage.getSender()).addOffrefournisseur(reponse.getOffre());

                return reponse;
            }
            // on refuse l'offre et on mes fins à la négociation
            return new refuseMessage(this, offerMessage.getSender(), "Je refuse votre offre, je ne veux plus négocier avec vous"); 
        }
    }
    
}