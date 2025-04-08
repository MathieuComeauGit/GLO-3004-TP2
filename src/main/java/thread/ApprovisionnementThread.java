package thread;

import domain.ChocolatierR;
import domain.enums.EtapeChocolatier;
import domain.enums.GroupeDeChocolatier;
import repository.ChocolatRepository;
import repository.ChocolatierRepository;

import java.util.List;
import java.util.Random;

public class ApprovisionnementThread extends Thread {
    private final ChocolatierRepository chocolatierRepository;
    private final ChocolatRepository chocolatRepository;
    private final Random random = new Random();

    public ApprovisionnementThread(ChocolatierRepository chocolatierRepository, ChocolatRepository chocolatRepository) {
        this.chocolatierRepository = chocolatierRepository;
        this.chocolatRepository = chocolatRepository;
    }

    @Override
    public void run() {
        try {
            while (true) {
                approvisionnerSiNecessaire(GroupeDeChocolatier.n);
                approvisionnerSiNecessaire(GroupeDeChocolatier.b);
                Thread.sleep(1000); 
            }
        } catch (InterruptedException e) {
            System.out.println("[APPRO] Thread interrompu.");
        }
    }

    private void approvisionnerSiNecessaire(GroupeDeChocolatier groupe) {
        if (chocolatRepository.getQuantite(groupe) == 0 ) {
            List<ChocolatierR> chocolatiers = chocolatierRepository.findAll();
            boolean tousEnRupture = chocolatiers.stream()
                    .filter(c -> c.getGroupeDeChocolatier() == groupe)
                    .allMatch(c -> c.getEtape() == EtapeChocolatier.RUPTURE);
    
            if (tousEnRupture) {
                int ajout = 5 + random.nextInt(6); 
                chocolatRepository.approvisionner(ajout, groupe);
                System.out.println("[APPRO] +"+ajout+" chocolats pour le groupe " + groupe.name());
            } 
        }
    }
}
