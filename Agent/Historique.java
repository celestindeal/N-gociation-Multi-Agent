package Agent;

import java.util.ArrayList;

import communication.Offre;

public class Historique {
    private ArrayList<Offre> Offrefournisseur = new ArrayList<Offre>();
    private ArrayList<Offre> OffreNegociateur = new ArrayList<Offre>() ;

    public Historique() {

    }
    
    public ArrayList<Offre> getOffrefournisseur() {
        return Offrefournisseur;
    }

    public ArrayList<Offre> getOffreNegociateur() {
        return OffreNegociateur;
    }

    public Offre addOffreFournisseur(Offre offre) {
        Offrefournisseur.add(offre);
        return offre;
    }

    public Offre addOffreNegociateur(Offre offre) {
        OffreNegociateur.add(offre);
        return offre;
    }
    
}
