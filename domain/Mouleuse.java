package domain;

import domain.enums.EtapeMouleuse;

import java.util.UUID;

public class Mouleuse {
    private int id;
    private UUID chocolatierUtilisantId;
    private EtapeMouleuse etape;

    public Mouleuse(int id) {
        this.id = id;
        this.etape = EtapeMouleuse.AUCUNE;
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
