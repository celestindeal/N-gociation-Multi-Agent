package Communication;


import Agent.AgentFournisseur;
import Agent.Service;

import java.util.ArrayList;

public final class PlacePublique {
    private static final ArrayList<Service> servicesEnVente = new ArrayList<Service>();
    private static PlacePublique instance;
    private static ArrayList<Service> servicesPossible = new ArrayList<Service>();
    private static int nbServices = 0;


    private PlacePublique() {
    }

    public static PlacePublique getInstance() {
        if (instance == null) {
            instance = new PlacePublique();
        }
        return instance;
    }

    public ArrayList<Service> getServicesPossible() {
        return servicesPossible;
    }

    public static void setServicesPossible(ArrayList<Service> servicesPossible) {
        PlacePublique.servicesPossible = servicesPossible;
    }

    public Service getService(int agentID) {
        return servicesPossible.get(agentID);
    }

    public void buyService(Service service) {
        service.setServiceID(nbServices);
        nbServices++;
        System.out.println("[Place Publique]\t\t\tMISE EN VENTE : le service " + service.getServiceID() + " vendu par le fournisseur " + service.getAgentFournisseur().getAgentID() + " au prix de " + service.getPrix() + "€");
        servicesEnVente.add(service);
    }

    public void removeService(Service service) {
        System.out.println("[Place Publique]\t\t\tRETRAIT DE LA VENTE : le service " + service.getServiceID() + " vendu par " + service.getAgentFournisseur().getAgentID() + " a été retiré de la vente");
        servicesEnVente.remove(service);
    }

    public ArrayList<Service> getServiceAvendre() {
        return servicesEnVente;
    }

    public ArrayList<Service> getServiceVenduFournisseur(AgentFournisseur agent) {
        ArrayList<Service> serviceVenduFournisseur = new ArrayList<Service>();
        for (Service service : servicesEnVente) {
            if (service.getAgentFournisseur() == agent) {
                serviceVenduFournisseur.add(service);
            }
        }
        return serviceVenduFournisseur;
    }

}