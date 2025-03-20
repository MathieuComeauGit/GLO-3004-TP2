package domain;

import domain.enums.EtapeTempereuse;
import domain.enums.GroupeDeChocolatier;

public class Tempereuse extends Machine<EtapeTempereuse> {
    public Tempereuse(int id, GroupeDeChocolatier groupeDeChocolatier) {
        super(id, groupeDeChocolatier, EtapeTempereuse.AUCUNE);
    }

    @Override
    public void liberer() {
        super.liberer();
        etape = EtapeTempereuse.AUCUNE;
    }
}
