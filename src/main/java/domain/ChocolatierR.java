package domain;
import java.util.UUID;

import domain.enums.*;

public class ChocolatierR extends AbstractModel<EtapeChocolatier> {
    public ChocolatierR(GroupeDeChocolatier groupeDeChocolatier) {
        super(UUID.randomUUID(), groupeDeChocolatier, EtapeChocolatier.AUCUNE);
    }
}
