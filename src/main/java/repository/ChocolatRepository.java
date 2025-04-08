package repository;

import domain.enums.GroupeDeChocolatier;

public class ChocolatRepository {
    private int quantiteN;
    private int quantiteB;

    public ChocolatRepository() {
        this.quantiteN = 0;
        this.quantiteB = 0;

    }

    public void setQuantiteN(int quantiteN) {
        this.quantiteN = quantiteN;
    }

    public void setQuantiteB(int quantiteB) {
        this.quantiteB = quantiteB;
    }
    public boolean peutConsommer(GroupeDeChocolatier groupeDeChocolatier) {
        if (groupeDeChocolatier == GroupeDeChocolatier.n) {
            if (quantiteN <= 0) {
                return false;
            } else {
                return true;
            }
        }
        else {
            if (quantiteB <= 0) {
                return false;
            } else {
                return true;
            }
        }

    }

    public boolean consommer(GroupeDeChocolatier groupeDeChocolatier) {
        if (groupeDeChocolatier == GroupeDeChocolatier.n) {
            if (quantiteN > 0) {
                quantiteN -= 1;
                return true;
            } else {
                return false;
            }
        }
        else {
            if (quantiteB > 0) {
                quantiteB -= 1;
                return true;
            } else {
                return false;
            }
        }

    }

    public void approvisionner(int quantiteAjoutee, GroupeDeChocolatier groupeDeChocolatier) {
        if (groupeDeChocolatier == GroupeDeChocolatier.n) {
            quantiteN += quantiteAjoutee;
        }
        else {
            quantiteB += quantiteAjoutee;
        }
    }

    public int getQuantite(GroupeDeChocolatier groupeDeChocolatier) {
        if (groupeDeChocolatier == GroupeDeChocolatier.n) {
            return quantiteN;
        }
        else {
            return quantiteB;
        }

    }

    public void clear() {
        this.quantiteN = 0;
        this.quantiteB = 0;
    }
}
