package Agent;

import Communication.Offre;

import java.util.ArrayList;

public class Historique {
    private ArrayList<Offre> offrefournisseur = new ArrayList<Offre>();
    private ArrayList<Offre> offreNegociateur = new ArrayList<Offre>();
    private Service service;
    private double targetPrice;

    public Historique(Service service, double targetPrice) {
        this.service = service;
        this.targetPrice = targetPrice;
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
