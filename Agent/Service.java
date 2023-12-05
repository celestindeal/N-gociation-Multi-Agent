package Agent;

public class Service {

    private AgentFournisseur agentFournisseur;
    private int serviceID;
    private double prix;

    public Service(int serviceID, AgentFournisseur agentFournisseur, double prix) {
        this.serviceID = serviceID;
        this.agentFournisseur = agentFournisseur;
        this.prix = prix;
    }

    public AgentFournisseur getAgentFournisseur() {
        return agentFournisseur;
    }

    public void setAgentFournisseur(AgentFournisseur agentFournisseur) {
        this.agentFournisseur = agentFournisseur;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getServiceID() {
        return serviceID;
    }

    public double getPrix() {
        return prix;
    }
}
