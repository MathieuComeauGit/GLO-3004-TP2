package thread;

import domain.Tempereuse;
import service.TempereuseService;

import java.util.Random;
import java.util.UUID;

public class TempereuseThread extends Thread {
    private final UUID id;
    private final String nom;
    private final TempereuseService tempereuseService;
    private final Random random = new Random();

    public TempereuseThread(UUID id, String nom, TempereuseService tempereuseService) {
        this.id = id;
        this.nom = nom;
        this.tempereuseService = tempereuseService;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Tempereuse t = tempereuseService.getMachineById(id);

                if (t != null && t.getChocolatierUtilisantId() != null) {
                    int sleepTime = 1000 + random.nextInt(4000); // entre 1 et 5 sec
                    System.out.println("[TEMPEREUSE " + nom + "] En cours de tempérage... (" + sleepTime + "ms)");
                    Thread.sleep(sleepTime);

                    tempereuseService.avancerEtapeParMachineId(id);
                    System.out.println("[TEMPEREUSE " + nom + "] Étape avancée.");
                }

                Thread.sleep(500); // petit polling
            } catch (Exception e) {
                System.err.println("[TEMPEREUSE " + nom + "] Erreur : " + e.getMessage());
            }
        }
    }
}
