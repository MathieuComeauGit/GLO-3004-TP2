package domain;

import java.util.UUID;

import domain.enums.GroupeDeChocolatier;

public abstract class AbstractModel<E extends Enum<E>> {
    protected UUID id;
    protected GroupeDeChocolatier groupeDeChocolatier;
    protected E etape;

    public AbstractModel(UUID id, GroupeDeChocolatier groupeDeChocolatier, E etapeInitiale) {
        this.id = id;
        this.groupeDeChocolatier = groupeDeChocolatier;
        this.etape = etapeInitiale;
    }

    public UUID getId() {
        return id;
    }

    public GroupeDeChocolatier getGroupeDeChocolatier() {
        return groupeDeChocolatier;
    }

    public void setGroupeDeChocolatier(GroupeDeChocolatier groupeDeChocolatier) {
        this.groupeDeChocolatier = groupeDeChocolatier;
    }

    public E getEtape() {
        return etape;
    }

    public void setEtape(E etape) {
        this.etape = etape;
    }
}