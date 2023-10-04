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
        System.out.println("Service " + service.getServiceID() + " vendu par " + service.getAgentFournisseur().getAgentID());
        servicesEnVente.add(service);
    }

    public void removeService(Service service) {
        System.out.println("la liste de services en vente est de taille " + servicesEnVente.size() + " et contient le service ");
        for (Service s : servicesEnVente) {
            System.out.println(s.getServiceID());
        }
        System.out.println("Et moi je dois supprimer le service " + service.getServiceID() + " vendu par " + service.getAgentFournisseur().getAgentID());
        servicesEnVente.remove(service);
        System.out.println("maintenant la liste de services en vente est de taille " + servicesEnVente.size() );
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

        System.out.println("Fournisseur " + agent.getAgentID() + " : J'ai " + serviceVenduFournisseur.size() + " services en vente");
        return serviceVenduFournisseur;
    }

}