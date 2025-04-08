package thread;

import domain.Mouleuse;
import service.MouleuseService;

import java.util.Random;
import java.util.UUID;

public class MouleuseThread extends Thread {
    private final UUID id;
    private final String nom;
    private final MouleuseService mouleuseService;
    private final Random random = new Random();

    public MouleuseThread(UUID id, String nom, MouleuseService mouleuseService) {
        this.id = id;
        this.nom = nom;
        this.mouleuseService = mouleuseService;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Mouleuse m = mouleuseService.getMachineById(id);

                if (m != null && m.getChocolatierUtilisantId() != null) {
                    int sleepTime = 1000 + random.nextInt(4000); // 1 à 5 sec
                    System.out.println("[MOULEUSE " + nom + "] Traitement en cours... (" + sleepTime + "ms)");
                    Thread.sleep(sleepTime);

                    mouleuseService.avancerEtapeParMachineId(id);
                    System.out.println("[MOULEUSE " + nom + "] Étape avancée.");
                }

                Thread.sleep(500); // polling
            } catch (Exception e) {
                System.err.println("[MOULEUSE " + nom + "] Erreur : " + e.getMessage());
            }
        }
    }
}
