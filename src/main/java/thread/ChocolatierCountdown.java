package thread;

import domain.enums.EtapeChocolatier;
import domain.enums.GroupeDeChocolatier;

import java.util.concurrent.CountDownLatch;

public class ChocolatierCountdown {
    private final int nombreDeChocolatiers;
    private final GroupeDeChocolatier groupeDeChocolatier;

    private CountDownLatch[] PasDepasseTempereChocolat;
    private CountDownLatch[] PasDepasseDonneChocolat;
    private CountDownLatch[] PasDepasseRemplit;
    private CountDownLatch[] PasDepasseGarnit;
    private CountDownLatch[] PasDepasseFerme;

    public ChocolatierCountdown(int nombre, GroupeDeChocolatier groupeDeChocolatier) {
        this.nombreDeChocolatiers = nombre;
        this.groupeDeChocolatier = groupeDeChocolatier;
        this.PasDepasseTempereChocolat = new CountDownLatch[nombre];
        this.PasDepasseDonneChocolat = new CountDownLatch[nombre];
        this.PasDepasseRemplit = new CountDownLatch[nombre];
        this.PasDepasseGarnit = new CountDownLatch[nombre];
        this.PasDepasseFerme = new CountDownLatch[nombre];
    }

    public void resetCountdown() {
        for (int i = 0; i < nombreDeChocolatiers; i++) { // Nombre de threads
            PasDepasseTempereChocolat[i] = new CountDownLatch(1);
            PasDepasseDonneChocolat[i] = new CountDownLatch(1);
            PasDepasseRemplit[i] = new CountDownLatch(1);
            PasDepasseGarnit[i] = new CountDownLatch(1);
            PasDepasseFerme[i] = new CountDownLatch(1);
        }
    }

    public int getNombreDeChocolatiers() { return nombreDeChocolatiers; }
    public GroupeDeChocolatier getGroupe() { return groupeDeChocolatier; }

    public void updateCountdown(int position, EtapeChocolatier etape) {
        try {
            switch (etape) {
                case TEMPERE_CHOCOLAT:
                    if (position != 0) {
                        PasDepasseTempereChocolat[position - 1].await();
                    }

                    PasDepasseTempereChocolat[position].countDown();
                    break;

                case DONNE_CHOCOLAT:
                    if (position != 0) {
                        PasDepasseDonneChocolat[position - 1].await();
                    }

                    PasDepasseDonneChocolat[position].countDown();
                    break;

                case REMPLIT:
                    if (position != 0) {
                        PasDepasseRemplit[position - 1].await();
                    }
                    PasDepasseRemplit[position].countDown();
                    break;

                case GARNIT:
                    if (position != 0) {
                        PasDepasseGarnit[position - 1].await();
                    }
                    PasDepasseGarnit[position].countDown();

                    break;

                case FERME:
                    if (position != 0) {
                        PasDepasseFerme[position - 1].await();
                    }
                    PasDepasseFerme[position].countDown();
                    break;

                default:
                    break;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
