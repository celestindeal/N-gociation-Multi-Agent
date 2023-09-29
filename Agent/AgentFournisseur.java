package Agent;
import java.util.List;

// Classe pour les agents fournisseurs
public class AgentFournisseur extends Agent {
    private List<Service> servicesProposes;

    public AgentFournisseur(String agentID, List<Preference> preferences, List<Contrainte> contraintes) {
        super(agentID, "Fournisseur", preferences, contraintes);
    }


    public List<Service> getServicesProposes() {
        return servicesProposes;
    }

    public List<Contrainte> getContraintes() {
        return contraintes;
    }

    public void setContraintes(List<Contrainte> contraintes) {
        this.contraintes = contraintes;
    }

    public List<Preference> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<Preference> preferences) {
        this.preferences = preferences;
    }
}