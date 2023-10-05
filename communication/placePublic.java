package communication;


import java.util.ArrayList;

import Agent.AgentFournisseur;
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
        System.out.println("MISE EN VENTE : le service " + service.getServiceID() + " vendu par " + service.getAgentFournisseur().getAgentID());
        servicesEnVente.add(service);
    }

    public void removeService(Service service) {
        System.out.println("RETRAIT DE LA VENTE : le service " + service.getServiceID() + " vendu par " + service.getAgentFournisseur().getAgentID() + " a été retiré de la vente");
        for (Service s : servicesEnVente) {
            System.out.println(s.getServiceID());
        }
        servicesEnVente.remove(service);
    }

    public ArrayList<Service> getServiceAvendre() {
        return servicesEnVente;
    }

    public ArrayList<Service> getserviceVenduFournisseur(AgentFournisseur agent) {
        ArrayList<Service> serviceVenduFournisseur = new ArrayList<Service>();
        for(Service service : servicesEnVente) {
            if(service.getAgentFournisseur() == agent) {
                serviceVenduFournisseur.add(service);
            }
        }
        return serviceVenduFournisseur;
    }

}