package domain;

import domain.enums.EtapeMouleuse;
import domain.enums.GroupeDeChocolatier;

public class Mouleuse extends Machine<EtapeMouleuse> {

    public Mouleuse(int id, GroupeDeChocolatier groupeDeChocolatier) {
        super(id, groupeDeChocolatier, EtapeMouleuse.AUCUNE);
    }

    @Override
    public void liberer() {
        super.liberer();
        etape = EtapeMouleuse.AUCUNE;
    }
}
