package domain;

import domain.enums.EtapeTempereuse;
import domain.enums.GroupeDeChocolatier;

import java.util.UUID;

public class Tempereuse {
    private int id;
    private UUID chocolatierUtilisantId;
    private EtapeTempereuse etape;
    private GroupeDeChocolatier groupeDeChocolatier;

    public Tempereuse(int id, GroupeDeChocolatier groupeDeChocolatier) {
        this.id = id;
        this.etape = EtapeTempereuse.AUCUNE;
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

    public EtapeTempereuse getEtape() {
        return etape;
    }

    public void setEtape(EtapeTempereuse etape) {
        this.etape = etape;
    }

    public boolean estDisponible() {
        return chocolatierUtilisantId == null;
    }

    public void liberer() {
        chocolatierUtilisantId = null;
        etape = EtapeTempereuse.AUCUNE;
    }
}
