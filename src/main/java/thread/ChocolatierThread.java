package thread;

import domain.enums.GroupeDeChocolatier;
import service.ChocolatierService;

import java.util.Random;
import java.util.UUID;

public class ChocolatierThread extends Thread {
    private final String id;
    private final GroupeDeChocolatier groupe;
    private final ChocolatierService chocolatierService;
    private final Random rand = new Random();

    public ChocolatierThread(String id, GroupeDeChocolatier groupe, ChocolatierService chocolatierService) {
        this.id = id;
        this.groupe = groupe;
        this.chocolatierService = chocolatierService;
    }

    public String getChocolatierId() { return id; }
    public GroupeDeChocolatier getGroupe() { return groupe; }

    @Override
    public void run() {
        try {
            UUID uuid = UUID.fromString(id);
            while (true) {
                boolean avance = chocolatierService.avancerEtape(uuid);
                if (avance) {
                    System.out.println("[CHOCOLATIER " + id + "] a avancé d'une étape.");
                } else {
                    System.out.println("[CHOCOLATIER " + id + "] est bloqué ou aucune progression possible.");
                }

                Thread.sleep(rand.nextInt(5_000) + 1_000); // entre 1 et 5 secondes
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
