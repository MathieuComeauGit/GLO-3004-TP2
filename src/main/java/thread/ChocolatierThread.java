package thread;

import domain.enums.GroupeDeChocolatier;
import service.ChocolatierService;
import service.SimulationService;

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
        synchronized (SimulationService.class) {
            try {
                UUID uuid = UUID.fromString(id);
                while (true) {
                    if (SimulationService.isCurrentType(groupe)) {
                        chocolatierService.avancerEtape(uuid);
                        Thread.sleep(rand.nextInt(5_000) + 1_000); 
                    }
                    else {
                        this.wait(); 
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
