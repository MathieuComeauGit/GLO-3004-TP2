package domain;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import domain.enums.GroupeDeChocolatier;

public abstract class AbstractMachine<E extends Enum<E>> extends AbstractModel<E> {
    protected UUID chocolatierUtilisantId;
    protected LinkedList<ChocolatierR> listeAttente = new LinkedList<>();

    public AbstractMachine(UUID id, GroupeDeChocolatier groupeDeChocolatier, E etapeInitiale) {
        super(id, groupeDeChocolatier, etapeInitiale);
    }

    public UUID getChocolatierUtilisantId() {
        return chocolatierUtilisantId;
    }

    public void setChocolatierUtilisantId(UUID chocolatierUtilisantId) {
        this.chocolatierUtilisantId = chocolatierUtilisantId;
    }

    /* Fonctions pour la liste d'attente pour l'utilisation de la tempereuse */
    public List<ChocolatierR> getListeAttente() {
        return listeAttente;
    }

    public void ajouteChocolatierListeAttente(ChocolatierR chocolatier) {
        listeAttente.addLast(chocolatier);
    }

    public void retirerChocolatierListeAttente(ChocolatierR chocolatier) {
        listeAttente.remove(chocolatier);
    }

    public ChocolatierR prochainChocolatier() {
        return listeAttente.getFirst();
    }

    public void viderListeAttente() {
        listeAttente.clear();
    }

    public boolean listeAttenteVide() {
        return listeAttente.isEmpty();
    }

    /* Fonctions utilitaires */
    public boolean estDisponible() {
        return chocolatierUtilisantId == null;
    }

    public void rendreDisponible() {
        chocolatierUtilisantId = null;
    }
}