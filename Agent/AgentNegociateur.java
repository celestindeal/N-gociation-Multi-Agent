package Agent;
import java.util.List;

// Classe pour les agents négociateurs
public class AgentNegociateur extends Agent {

    private StrategieNegociation strategieNegociation;

    public AgentNegociateur(String agentID, List<Preference> preferences, List<Contrainte> contraintes) {
        super(agentID,"Negociateur", preferences, contraintes);
    }

    public List<Preference> getPreferencesUtilisateur() {
        return preferences;
    }

    public void setPreferencesUtilisateur(List<Preference> preferencesUtilisateur) {
        this.preferences = preferencesUtilisateur;
    }

    public List<Contrainte> getContraintesUtilisateur() {
        return contraintes;
    }

    public void setContraintesUtilisateur(List<Contrainte> contraintesUtilisateur) {
        this.contraintes = contraintesUtilisateur;
    }

    public StrategieNegociation getStrategieNegociation() {
        return strategieNegociation;
    }

    public void setStrategieNegociation(StrategieNegociation strategieNegociation) {
        this.strategieNegociation = strategieNegociation;
    }
}