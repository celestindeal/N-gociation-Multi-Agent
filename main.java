import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Agent.AgentFournisseur;
import Agent.AgentNegociateur;
import Agent.Contrainte;
import Agent.Preference;
import communication.placePublic;




public class main {

    public static void main(String[] args) {
        
        ArrayList<Thread> fournisseur = new ArrayList<>();
        ArrayList<Thread> negociateur = new ArrayList<>();

         // créer 3 services à la place 
        ArrayList<Agent.Service> services = new ArrayList<Agent.Service>();
        for(int i = 0; i < 1; i++){
            Agent.Service s = new Agent.Service(i, null, Double.NEGATIVE_INFINITY );
            services.add(s);
        }

        placePublic.getInstance(services);


        // créer 3 agents fournisseurs
        for (int i = 0; i < 1; i++) {

            List<Preference> preferences = new ArrayList<Preference>();
            List<Contrainte> contraintes = new ArrayList<Contrainte>();

            AgentFournisseur f = new AgentFournisseur(i, preferences, contraintes);
            Thread thread = new Thread(f);
            fournisseur.add(thread);

            thread.start(); 
        }

        System.out.println("Agents fournisseurs créés");
        // créer 3 agents clients
        Random random = new Random();
        
        for(int i = 0; i < 2; i++){
            List<Preference> preferences = new ArrayList<Preference>();
            List<Contrainte> contraintes = new ArrayList<Contrainte>();
            double valeurAleatoire = random.nextInt(1000) + 4000;
            AgentNegociateur f = new AgentNegociateur(i, preferences, contraintes, valeurAleatoire);
            Thread thread = new Thread(f);
            negociateur.add(thread);

            thread.start(); 
        }

        System.out.println("Agents négociateurs créés");

       

    }
}

