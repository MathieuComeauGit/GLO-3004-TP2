package domain;

import domain.enums.EtapeMouleuse;
import domain.enums.GroupeDeChocolatier;

import java.util.UUID;

public class Mouleuse {
    private int id;
    private UUID chocolatierUtilisantId;
    private EtapeMouleuse etape;
    private GroupeDeChocolatier groupeDeChocolatier;

    public Mouleuse(int id, GroupeDeChocolatier groupeDeChocolatier) {
        this.id = id;
        this.etape = EtapeMouleuse.AUCUNE;
        this.groupeDeChocolatier = groupeDeChocolatier;
    }

    public int getId() {
        return id;
    }

    public GroupeDeChocolatier getGroupeDeChocolatier() {
        return groupeDeChocolatier;
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
