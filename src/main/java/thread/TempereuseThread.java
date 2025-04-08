package thread;

import service.TempereuseService;

import java.util.Random;
import java.util.UUID;

public class TempereuseThread extends Thread {
    private final UUID id;
    private final TempereuseService tempereuseService;
    private final Random rand = new Random();

    public TempereuseThread(UUID id, String nom, TempereuseService tempereuseService) {
        this.id = id;
        this.tempereuseService = tempereuseService;
    }

    @Override
    public void run() {
        try {
            while (true) {
                tempereuseService.avancerEtapeParMachineId(id);
                Thread.sleep(1000 + rand.nextInt(5000)); 
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("[TEMPEREUSE] Erreur : " + e.getMessage());
        }
    }
}
