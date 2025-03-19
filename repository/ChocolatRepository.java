package repository;

public class ChocolatRepository {
    private int quantite;

    public ChocolatRepository(int quantiteInitiale) {
        this.quantite = quantiteInitiale;
    }

    public boolean consommer(int quantiteDemandee) {
        if (quantite >= quantiteDemandee) {
            quantite -= quantiteDemandee;
            return true;
        } else {
            return false;
        }
    }

    public void approvisionner(int quantiteAjoutee) {
        quantite += quantiteAjoutee;
    }

    public int getQuantite() {
        return quantite;
    }
}
