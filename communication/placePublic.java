package communication;

import java.util.ArrayList;
import Agent.Service;

public final class placePublic {
    private static placePublic instance;
    private static ArrayList<Service> servicesPossible = new ArrayList<Service>();
    private static ArrayList<Service> servicesEnVente = new ArrayList<Service>();


    private placePublic(ArrayList<Service> services) {
        this.servicesPossible = services;       
    }

    public static placePublic getInstance(ArrayList<Service> services) {
        if (instance == null) {
            instance = new placePublic(services);
        }
        return instance;
    }

    public ArrayList<Service> getServicesPossible() {
        return servicesPossible;
    }

    public Service getService(int agentID) {
        return servicesPossible.get(agentID);
    }

    public void buyService(Service service) {
        servicesEnVente.add(service);
    }

    public void removeService(Service service) {
        System.out.println("Service " + service.getServiceID() + " supprimé de la place publique");
        servicesEnVente.remove(service);
    }

    public ArrayList<Service> getServiceAvendre() {
        return servicesEnVente;
    }

}