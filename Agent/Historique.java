package Agent;

import java.util.ArrayList;

import communication.Offre;

public class Historique {
    private ArrayList<Offre> Offrefournisseur ;
    private ArrayList<Offre> OffreNegociateur ;

    public Historique( ArrayList<Offre> Offrefournisseur, ArrayList<Offre> OffreNegociateur) {
        this.Offrefournisseur = Offrefournisseur;
        this.OffreNegociateur = OffreNegociateur;
    }
    
    public ArrayList<Offre> getOffrefournisseur() {
        return Offrefournisseur;
    }

    public ArrayList<Offre> getOffreNegociateur() {
        return OffreNegociateur;
    }

    public Offre addOffrefournisseur(Offre offre) {
        Offrefournisseur.add(offre);
        return offre;
    }

    public Offre addOffreNegociateur(Offre offre) {
        OffreNegociateur.add(offre);
        return offre;
    }
    
}
