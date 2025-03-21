package domain;

import java.util.UUID;
import domain.enums.EtapeMouleuse;
import domain.enums.GroupeDeChocolatier;

public class Mouleuse extends Machine<EtapeMouleuse>{

    public Mouleuse(UUID id, GroupeDeChocolatier groupeDeChocolatier) {
        super(id, groupeDeChocolatier, EtapeMouleuse.AUCUNE);
    }

    @Override
    public void rendreDisponible() {
        super.rendreDisponible();
        etape = EtapeMouleuse.AUCUNE;
    }
}
