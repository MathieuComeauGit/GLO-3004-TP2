package domain;

import java.util.UUID;
import domain.enums.EtapeChocolatier;
import domain.enums.GroupeDeChocolatier;

public class ChocolatierR extends AbstractModel<EtapeChocolatier> {
    public ChocolatierR(UUID id, GroupeDeChocolatier groupeDeChocolatier) {
        super(id, groupeDeChocolatier, EtapeChocolatier.AUCUNE);
    }
}
