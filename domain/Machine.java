package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import domain.enums.GroupeDeChocolatier;

public abstract class Machine<T> {
    protected int id;
    protected UUID chocolatierUtilisantId;
    protected GroupeDeChocolatier groupeDeChocolatier;
    protected List<ChocolatierR> listeAttente = new ArrayList<>();
    protected T etape;

    public Machine(int id, GroupeDeChocolatier groupeDeChocolatier, T etape) {
        this.id = id;
        this.groupeDeChocolatier = groupeDeChocolatier;
        this.etape = etape;
    }

    public int getId() {
        return id;
    }

    public GroupeDeChocolatier getGroupeDeChocolatier() {
        return groupeDeChocolatier;
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

    public void liberer() {
        chocolatierUtilisantId = null;
    }
}
