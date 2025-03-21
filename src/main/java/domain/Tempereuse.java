package domain;

import java.util.UUID;
import domain.enums.EtapeTempereuse;
import domain.enums.GroupeDeChocolatier;

public class Tempereuse extends Machine<EtapeTempereuse>{

    public Tempereuse(UUID id, GroupeDeChocolatier groupeDeChocolatier) {
        super(id, groupeDeChocolatier, EtapeTempereuse.AUCUNE);
    }

    @Override
    public void rendreDisponible() {
        super.rendreDisponible();
        etape = EtapeTempereuse.AUCUNE;
    }
}
