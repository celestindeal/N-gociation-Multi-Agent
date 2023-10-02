package Agent;
import java.util.List;

import communication.Message;
import communication.OfferMessage;
import communication.placePublic;
import communication.refuseMessage;
import communication.valideMessage;

// Classe pour les agents négociateurs
public class AgentNegociateur extends Agent {

    private Double money; 


    public AgentNegociateur(int agentID, List<Preference> preferences, List<Contrainte> contraintes, Double money) {
        super(agentID,"Negociateur", preferences, contraintes);
        this.money = money;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(2000); 
                rechercheService();
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
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
        //choisir un service aléatoirement et ce fixer un prix pour l'achat de ce service
        List<Service> services = placePublic.getInstance(null).getServiceAvendre();
        if(services.isEmpty()){
            System.out.println("Il n'y a pas de service à vendre");
            return;
        }
        
        int randomIndex = (int) (Math.random() * services.size());

        Service service = services.get(randomIndex);

        if (service == null) {
            System.out.println("Le service n'est pas encore en vente");
            return;
        }

        
        System.out.println(service);
        System.out.println("Je suis le négociateur " + this.agentID + "Je possède " + money + "€ Je veux acheter le service " + service.getServiceID());

        Double prixServise = service.getPrix();
        // stratégie du prix d'achat 
        // si le prix est de moin de la moitié de l'argent que l'agent possède alors il achète le service au prix 
        // si le prix est de plus de la moitié de l'argent que l'agent possède mais moins que l'argent que l'agent possède alors il achète le service au prix - 10% du prix
        // si les prix est à plus que l'argent que l'agent possède (+20%) je négocie le prix
        // si le prix est plus que l'argent que l'agent possède (+20%) alors je n'ache pas le service
        if (prixServise < (money/2)){
            acheterServiceAuPrix(service);
        }else if (prixServise > (money/2) && prixServise < money){
            System.out.println("Je suis le négociateur " + this.agentID +"Je achète le service avec une faible négociation");
            faibleNegociation(service);
        }else if (prixServise > money && prixServise < money*1.2){
            System.out.println("Je suis le négociateur " + this.agentID +"Je négocie le prix");

        }else if (prixServise > money*1.2){
            System.out.println("Je suis le négociateur " + this.agentID +"Je n'achète pas le service");
        }
    }

    public void acheterServiceAuPrix(Service service) {

        System.out.println("Je suis le négociateur " + this.agentID +"Je achète le service" + " au prix de " + service.getPrix() + " Car je possède " + money + "€");
        
        AgentFournisseur agentFournisseur = service.getAgentFournisseur();
        OfferMessage offerMessage = new OfferMessage(this, agentFournisseur,service, service.getPrix());

        Message reponse =  agentFournisseur.receiveMessage(offerMessage);

        if(reponse instanceof valideMessage){
            System.out.println("Le fournisseur a accepté l'offre");
                money = money - service.getPrix();
        }else{
            System.out.println("Le fournisseur a refusé l'offre");
        }
    }

    public void faibleNegociation(Service service){
        // on propose en boucle -20% et si on arrive à -10% on achète
        AgentFournisseur agentFournisseur = service.getAgentFournisseur();
        OfferMessage offerMessage = new OfferMessage(this, agentFournisseur,service, service.getPrix()*0.8);

        Message reponse =  agentFournisseur.receiveMessage(offerMessage);

        while (!(reponse instanceof refuseMessage)){

            if (reponse instanceof valideMessage){
                System.out.println("Le fournisseur a accepté l'offre");
                money = money - service.getPrix();
                return;
            }else if (reponse instanceof OfferMessage){
                Double prix = ((OfferMessage) reponse).getOffre().getPrix();
                if (prix < service.getPrix()*0.9){
                    System.out.println("Le Négociateur a accepté l'offre");
                    money = money - service.getPrix();
                    return;
                }
            }
            offerMessage = new OfferMessage(this, agentFournisseur,service, service.getPrix()*0.8);
            reponse =  agentFournisseur.receiveMessage(offerMessage);
        }
        System.out.println("Le fournisseur a refusé l'offre");
    }
}

