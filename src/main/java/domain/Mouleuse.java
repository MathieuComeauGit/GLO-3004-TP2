package domain;

import java.util.UUID;

import domain.enums.EtapeMouleuse;
import domain.enums.GroupeDeChocolatier;

public class Mouleuse {
    private UUID id;
    private UUID chocolatierUtilisantId;
    private EtapeMouleuse etape;
    private GroupeDeChocolatier groupeDeChocolatier;

    public Mouleuse(UUID id, GroupeDeChocolatier groupeDeChocolatier) {
        this.id = id;
        this.etape = EtapeMouleuse.AUCUNE;
        this.groupeDeChocolatier = groupeDeChocolatier;
    }

    public UUID getId() {
        return id;
    }

    public GroupeDeChocolatier getGroupeDeChocolatier() {
        return groupeDeChocolatier;
    }

    public void setGroupeDeChocolatier(GroupeDeChocolatier groupeDeChocolatier) {
        this.groupeDeChocolatier = groupeDeChocolatier;
    }

    public UUID getChocolatierUtilisantId() {
        return chocolatierUtilisantId;
    }

    public void setChocolatierUtilisantId(UUID chocolatierUtilisantId) {
        this.chocolatierUtilisantId = chocolatierUtilisantId;
    }

    public EtapeMouleuse getEtape() {
        return etape;
    }

    public void setEtape(EtapeMouleuse etape) {
        this.etape = etape;
    }

    public boolean estDisponible() {
        return chocolatierUtilisantId == null;
    }

    public void liberer() {
        chocolatierUtilisantId = null;
        etape = EtapeMouleuse.AUCUNE;
    }
}
