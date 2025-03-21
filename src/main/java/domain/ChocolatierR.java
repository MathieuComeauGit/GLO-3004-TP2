package domain;
import java.util.UUID;

import domain.enums.*;

public class ChocolatierR {
    private final UUID id;
    private final GroupeDeChocolatier provenance;
    private EtapeChocolatier etape;
    public ChocolatierR(GroupeDeChocolatier provenance) {
        this.id = UUID.randomUUID();
        this.provenance = provenance;
        this.etape = EtapeChocolatier.AUCUNE;
    }

    public UUID getId() {
        return id;
    }

    public GroupeDeChocolatier getProvenance() {
        return provenance;
    }

    public EtapeChocolatier getEtape() {
        return etape;
    }

    public void setEtape(EtapeChocolatier etape) {
        this.etape = etape;
    }

}
