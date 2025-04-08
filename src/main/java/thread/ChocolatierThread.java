package thread;

import domain.enums.GroupeDeChocolatier;
import service.ChocolatierService;
import service.SimulationService;

import java.util.Random;
import java.util.UUID;

public class ChocolatierThread extends Thread {
    private final UUID id;
    private final int position;
    private final GroupeDeChocolatier groupe;
    private final ChocolatierService chocolatierService;
    private final Random rand = new Random();

    public ChocolatierThread(UUID id, int position, GroupeDeChocolatier groupe, ChocolatierService chocolatierService) {
        this.id = id;
        this.position = position;
        this.groupe = groupe;
        this.chocolatierService = chocolatierService;
    }

    public UUID getChocolatierId() { return id; }
    public int getPosition() { return position; }
    public GroupeDeChocolatier getGroupe() { return groupe; }

    @Override
    public void run() {
        try {
            while (true) {
                if (SimulationService.isCurrentType(groupe)) {
                    chocolatierService.avancerEtape(id, position);
                    Thread.sleep(1000 + rand.nextInt(5000));
                }
            } 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("[CHOCOLATIER] Erreur : " + e.getMessage());
        }
    }
}
