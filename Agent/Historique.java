package Agent;

import Communication.Offre;

import java.util.ArrayList;

public class Historique {
    private ArrayList<Offre> offrefournisseur = new ArrayList<Offre>();
    private ArrayList<Offre> offreNegociateur = new ArrayList<Offre>();
    private Service service;
    private double targetPrice;
    private int nbMaxOffre;

    public Historique(Service service, double targetPrice) {
        this(service, targetPrice, 0);
    }

    public Historique(Service service, double targetPrice, int nbMaxOffre) {
        this.service = service;
        this.targetPrice = targetPrice;
        this.nbMaxOffre = nbMaxOffre;
    }

    public boolean getTimeValide() {
        return this.nbMaxOffre > offreNegociateur.size();
    }

    public ArrayList<Offre> getOffrefournisseur() {
        return offrefournisseur;
    }

    public ArrayList<Offre> getOffreNegociateur() {
        return offreNegociateur;
    }

    public Service getService() {
        return service;
    }

    public double getTargetPrice() {
        return targetPrice;
    }

}
