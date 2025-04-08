package thread;


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
            PasDepasseTempereChocolat[i] = new CountDownLatch(nombreDeChocolatiers);
            PasDepasseDonneChocolat[i] = new CountDownLatch(nombreDeChocolatiers);
            PasDepasseRemplit[i] = new CountDownLatch(nombreDeChocolatiers);
            PasDepasseGarnit[i] = new CountDownLatch(nombreDeChocolatiers);
            PasDepasseFerme[i] = new CountDownLatch(nombreDeChocolatiers);
        }
    }

    public int getNombreDeChocolatiers() { return nombreDeChocolatiers; }
    public GroupeDeChocolatier getGroupe() { return groupeDeChocolatier; }
    public CountDownLatch[] getPasDepasseTempereChocolat() { return PasDepasseTempereChocolat; }
    public CountDownLatch[] getPasDepasseDonneChocolat() { return PasDepasseDonneChocolat; }
    public CountDownLatch[] getPasDepasseRemplit() { return PasDepasseRemplit; }
    public CountDownLatch[] getPasDepasseGarnit() { return PasDepasseGarnit; }
    public CountDownLatch[] getPasDepasseFerme() { return PasDepasseFerme; }
}
