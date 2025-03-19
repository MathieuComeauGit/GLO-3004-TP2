package domain;

import domain.enums.EtapeTempereuse;

import java.util.UUID;

public class Tempereuse {
    private int id;
    private UUID chocolatierUtilisantId;
    private EtapeTempereuse etape;

    public Tempereuse(int id) {
        this.id = id;
        this.etape = EtapeTempereuse.AUCUNE;
    }

    public int getId() {
        return id;
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
