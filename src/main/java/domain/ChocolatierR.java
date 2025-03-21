package domain;
import java.util.UUID;

import domain.enums.*;

public class ChocolatierR {
    private final UUID id;
    private final GroupeDeChocolatier groupeDeChocolatier;
    private EtapeChocolatier etape;

    public ChocolatierR(GroupeDeChocolatier groupeDeChocolatier) {
        this.id = UUID.randomUUID();
        this.groupeDeChocolatier = groupeDeChocolatier;
        this.etape = EtapeChocolatier.AUCUNE;
    }

    public UUID getId() {
        return id;
    }

    public GroupeDeChocolatier getGroupeDeChocolatier() {
        return groupeDeChocolatier;
    }

    public EtapeChocolatier getEtape() {
        return etape;
    }

    public void setEtape(EtapeChocolatier etape) {
        this.etape = etape;
    }

}
