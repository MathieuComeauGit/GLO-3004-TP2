package thread;

import service.MouleuseService;

import java.util.Random;
import java.util.UUID;

public class MouleuseThread extends Thread {
    private final UUID id;
    private final MouleuseService mouleuseService;
    private final Random rand = new Random();

    public MouleuseThread(UUID id, String nom, MouleuseService mouleuseService) {
        this.id = id;
        this.mouleuseService = mouleuseService;
    }

    @Override
    public void run() {
        try {
            while (true) {
                mouleuseService.avancerEtapeParMachineId(id);
                Thread.sleep(1000 + rand.nextInt(5000));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("[MOULEUSE] Erreur : " + e.getMessage());
        }
    }
}
