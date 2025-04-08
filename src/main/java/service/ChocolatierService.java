package service;

import domain.ChocolatierR;
import domain.Mouleuse;
import domain.Tempereuse;
import domain.enums.EtapeChocolatier;
import domain.enums.EtapeTempereuse;
import domain.enums.GroupeDeChocolatier;
import repository.ChocolatierRepository;

import java.util.UUID;

public class ChocolatierService {
    private final ChocolatierRepository chocolatierRepository;
    private final TempereuseService tempereuseService;
    private final MouleuseService mouleuseService;

    public ChocolatierService(ChocolatierRepository chocolatierRepository,
            TempereuseService tempereuseService,
            MouleuseService mouleuseService) {
        this.chocolatierRepository = chocolatierRepository;
        this.tempereuseService = tempereuseService;
        this.mouleuseService = mouleuseService;
    }

    public boolean avancerEtape(UUID chocolatierId, int position) {
        ChocolatierR chocolatier = chocolatierRepository.findById(chocolatierId);
        if (chocolatier == null)
            return false;

        EtapeChocolatier current = chocolatier.getEtape();
        EtapeChocolatier next = null;

        try {
            switch (current) {
                case AUCUNE:
                    next = EtapeChocolatier.REQUIERE_TEMPEREUSE;
                    break;

                case REQUIERE_TEMPEREUSE:
                    Tempereuse temp = tempereuseService.getMachineDisponible(chocolatier.getGroupeDeChocolatier());
                    if (temp == null)
                        return false;
                    tempereuseService.requeteMachine(temp, chocolatier);
                    tempereuseService.assignerTempereuse(temp);
                    next = EtapeChocolatier.TEMPERE_CHOCOLAT;
                    break;

                case TEMPERE_CHOCOLAT:
                    if (position != 0) {
                        SimulationService.getCurrentCountdown().getPasDepasseTempereChocolat()[position - 1].await();
                    }

                    SimulationService.getCurrentCountdown().getPasDepasseTempereChocolat()[position].countDown();
                    // Chocolatier passe à DONNE_CHOCOLAT si la tempereuse est à DONNE_CHOCOLAT
                    Tempereuse temp1 = tempereuseService.getMachineAssociee(chocolatier.getId());
                    if (temp1 == null || temp1.getEtape() != EtapeTempereuse.DONNE_CHOCOLAT)
                        return false;
                    next = EtapeChocolatier.DONNE_CHOCOLAT;

                    break;

                case DONNE_CHOCOLAT:
                    if (position != 0) {
                        SimulationService.getCurrentCountdown().getPasDepasseDonneChocolat()[position - 1].await();
                    }

                    SimulationService.getCurrentCountdown().getPasDepasseDonneChocolat()[position].countDown();
                    // Attendre que la tempereuse passe à AUCUNE (libérée)
                    Tempereuse temp2 = tempereuseService.getMachineAssociee(chocolatier.getId());
                    if (temp2 != null)
                        return false;
                    next = EtapeChocolatier.REQUIERE_MOULEUSE;
                    break;

                case REQUIERE_MOULEUSE:
                    Mouleuse m = mouleuseService.getMachineDisponible(chocolatier.getGroupeDeChocolatier());
                    if (m == null)
                        return false;
                    mouleuseService.requeteMachine(m, chocolatier);
                    mouleuseService.assignerMouleuse(m, chocolatier.getId());
                    next = EtapeChocolatier.REMPLIT;
                    break;

                case REMPLIT:
                    if (position != 0) {
                        SimulationService.getCurrentCountdown().getPasDepasseRemplit()[position - 1].await();
                    }
                    SimulationService.getCurrentCountdown().getPasDepasseRemplit()[position].countDown();
                    Mouleuse moule = mouleuseService.getMachineAssociee(chocolatier.getId());
                    if (moule == null || moule.getEtape() != domain.enums.EtapeMouleuse.GARNIT)
                        return false;
                    next = EtapeChocolatier.GARNIT;
                    break;

                case GARNIT:
                    if (position != 0) {
                        SimulationService.getCurrentCountdown().getPasDepasseGarnit()[position - 1].await();
                    }
                    SimulationService.getCurrentCountdown().getPasDepasseGarnit()[position].countDown();
                    Mouleuse moule2 = mouleuseService.getMachineAssociee(chocolatier.getId());
                    if (moule2 == null || moule2.getEtape() != domain.enums.EtapeMouleuse.FERME)
                        return false;
                    next = EtapeChocolatier.FERME;
                    break;

                case FERME:
                    if (position != 0) {
                        SimulationService.getCurrentCountdown().getPasDepasseFerme()[position - 1].await();
                    }

                    SimulationService.getCurrentCountdown().getPasDepasseFerme()[position].countDown();
                    Mouleuse associee = mouleuseService.getMachineAssociee(chocolatier.getId());
                    if (associee != null)
                        mouleuseService.libererMachine(associee);
                    next = EtapeChocolatier.AUCUNE;

                    if (position == SimulationService.nombreChocolatiers - 1) {
                        // Reset the countdowns for the next run
                        SimulationService.getCurrentCountdown().resetCountdown();
                        // Switch to other groupe
                        SimulationService.changeCurrentType();
                    }
                    break;

                default:
                    return false;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        chocolatier.setEtape(next);
        return true;
    }

    public EtapeChocolatier getEtapeSuivantePossible(ChocolatierR chocolatier) {
        EtapeChocolatier[] etapes = EtapeChocolatier.values();
        int currentIndex = chocolatier.getEtape().ordinal();
        if (currentIndex + 1 >= etapes.length) return null;
        return etapes[currentIndex + 1];
    }

    public void initialiserChocolatiersGroupe(int nombre, GroupeDeChocolatier groupe) {
        chocolatierRepository.findAll().removeIf(c -> c.getGroupeDeChocolatier() == groupe);
        for (int i = 0; i < nombre; i++) {
            chocolatierRepository.save(new ChocolatierR(UUID.randomUUID(), groupe));
        }
    }

    public void reset() {
        chocolatierRepository.clear();
    }
}
