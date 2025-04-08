package thread;

import domain.enums.GroupeDeChocolatier;
import service.ChocolatierService;

import java.util.Random;
import java.util.UUID;

public class ChocolatierThread extends Thread {
    private final UUID id;
    private final GroupeDeChocolatier groupe;
    private final ChocolatierService chocolatierService;
    private final Random rand = new Random();

    public ChocolatierThread(String id, GroupeDeChocolatier groupe, ChocolatierService chocolatierService) {
        this.id = UUID.fromString(id);
        this.groupe = groupe;
        this.chocolatierService = chocolatierService;
    }

    @Override
    public void run() {
        try {
            while (true) {
                chocolatierService.avancerEtape(id);
                Thread.sleep(1000 + rand.nextInt(5000));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("[CHOCOLATIER] Erreur : " + e.getMessage());
        }
    }
}
