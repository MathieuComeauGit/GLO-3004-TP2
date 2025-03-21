package domain;

import java.util.UUID;

import domain.enums.EtapeTempereuse;
import domain.enums.GroupeDeChocolatier;

public class Tempereuse {
    private UUID id;
    private UUID chocolatierUtilisantId;
    private EtapeTempereuse etape;
    private GroupeDeChocolatier groupeDeChocolatier;

    public Tempereuse(UUID id, GroupeDeChocolatier groupeDeChocolatier) {
        this.id = id;
        this.etape = EtapeTempereuse.AUCUNE;
        this.groupeDeChocolatier = groupeDeChocolatier;
    }

    public UUID getId() {
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
