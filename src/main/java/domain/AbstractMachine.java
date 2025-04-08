package domain;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import domain.enums.GroupeDeChocolatier;

public abstract class AbstractMachine<E extends Enum<E>> extends AbstractModel<E> {
    protected UUID chocolatierUtilisantId;

    public AbstractMachine(UUID id, GroupeDeChocolatier groupeDeChocolatier, E etapeInitiale) {
        super(id, groupeDeChocolatier, etapeInitiale);
    }

    public UUID getChocolatierUtilisantId() {
        return chocolatierUtilisantId;
    }

    public void setChocolatierUtilisantId(UUID chocolatierUtilisantId) {
        this.chocolatierUtilisantId = chocolatierUtilisantId;
    }

    /* Fonctions utilitaires */
    public boolean estDisponible() {
        return chocolatierUtilisantId == null;
    }

    public void rendreDisponible() {
        chocolatierUtilisantId = null;
    }
}