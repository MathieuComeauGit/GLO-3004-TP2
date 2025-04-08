package service;

import domain.ChocolatierR;
import domain.Mouleuse;
import domain.enums.EtapeChocolatier;
import domain.enums.EtapeMouleuse;
import domain.enums.GroupeDeChocolatier;
import exceptions.BadCaseException;
import repository.ChocolatierRepository;
import repository.MouleuseRepository;

import java.util.UUID;

public class MouleuseService extends AbstractMachineService<EtapeMouleuse, Mouleuse, MouleuseRepository> {
    public MouleuseService(MouleuseRepository mouleuseRepository, ChocolatierRepository chocolatierRepository) {
        super(mouleuseRepository, chocolatierRepository);
    }

    public void assignerMouleuse(Mouleuse mouleuse, UUID chocolatierId) {
        super.assignerMachine(mouleuse, EtapeMouleuse.REMPLIT);
    }

    public boolean avancerEtapeParMachineId(UUID mouleuseId) throws BadCaseException {
        Mouleuse mouleuse = getMachineById(mouleuseId);
        if (mouleuse == null || mouleuse.getChocolatierUtilisantId() == null)
            return false;

        EtapeMouleuse current = mouleuse.getEtape();
        UUID chocolatierAssocieId = mouleuse.getChocolatierUtilisantId();
        ChocolatierR chocolatierAssocie = chocolatierRepository.findById(chocolatierAssocieId);

        switch (current) {
            case BLOCKED:
                return false;

            case AUCUNE:
                if (chocolatierAssocie != null) {
                    mouleuse.setEtape(EtapeMouleuse.REMPLIT);
                }
                break;

            case REMPLIT:
                mouleuse.setEtape(EtapeMouleuse.GARNIT);
                if (chocolatierAssocie != null)
                    chocolatierAssocie.setEtape(EtapeChocolatier.GARNIT);
                break;

            case GARNIT:
                mouleuse.setEtape(EtapeMouleuse.FERME);
                if (chocolatierAssocie != null)
                    chocolatierAssocie.setEtape(EtapeChocolatier.FERME);
                break;

            case FERME:
                mouleuse.setEtape(EtapeMouleuse.AUCUNE);
                if (chocolatierAssocie != null)
                    chocolatierAssocie.setEtape(EtapeChocolatier.AUCUNE);
                mouleuse.setChocolatierUtilisantId(null);
                break;

            default:
                throw new BadCaseException("Mouleuse: état non pris en charge");
        }

        return true;
    }

    public void initialiserMachineGroupe(int nombre, GroupeDeChocolatier groupe) {
        machineRepository.findAll().removeIf(m -> m.getGroupeDeChocolatier() == groupe);
        for (int i = 0; i < nombre; i++) {
            UUID id = UUID.randomUUID();
            machineRepository.save(new Mouleuse(id, groupe));
        }
    }

    public EtapeMouleuse getEtapeSuivantePossible(Mouleuse mouleuse) {
        EtapeMouleuse[] etapes = EtapeMouleuse.values();
        int currentIndex = mouleuse.getEtape().ordinal();
        if (currentIndex + 1 >= etapes.length) return null;
        return etapes[currentIndex + 1];
    }
}
