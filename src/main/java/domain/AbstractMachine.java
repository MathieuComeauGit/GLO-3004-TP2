package domain;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import domain.enums.GroupeDeChocolatier;

public abstract class AbstractMachine<T> {
    protected UUID id;
    protected UUID chocolatierUtilisantId;
    protected GroupeDeChocolatier groupeDeChocolatier;
    protected LinkedList<ChocolatierR> listeAttente = new LinkedList<>();
    protected T etape;

    public AbstractMachine(UUID id, GroupeDeChocolatier groupeDeChocolatier, T etapeInitiale) {
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

    public UUID getChocolatierUtilisantId() {
        return chocolatierUtilisantId;
    }

    public void setChocolatierUtilisantId(UUID chocolatierUtilisantId) {
        this.chocolatierUtilisantId = chocolatierUtilisantId;
    }

    public T getEtape() {
        return etape;
    }

    public void setEtape(T etape) {
        this.etape = etape;
    }

    /* Fonctions pour la liste d'attente pour l'utilisation de la tempereuse */
    public List<ChocolatierR> getListeAttente() {
        return listeAttente;
    }

    public void ajouteChocolatierListeAttente(ChocolatierR chocolatier) {
        listeAttente.add(chocolatier);
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