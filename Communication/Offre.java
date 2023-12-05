package Communication;

import Agent.Service;

public class Offre {
    private Service service;
    private double prix;

    public Offre(Service service, double prix) {
        this.service = service;
        this.prix = prix;
    }

    public Service getService() {
        return service;
    }

    public double getPrix() {
        return prix;
    }

}
