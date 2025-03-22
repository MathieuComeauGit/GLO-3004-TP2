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
        EtapeMouleuse next;
        UUID chocolatierAssocieId = mouleuse.getChocolatierUtilisantId();
        ChocolatierR chocolatierAssocie = chocolatierRepository.findById(chocolatierAssocieId);
        switch (current) {
            case BLOCKED: 
                return false;
            case AUCUNE:
                next = EtapeMouleuse.REMPLIT;
                mouleuse.setEtape(next);
                break;
            case REMPLIT:
                next = EtapeMouleuse.GARNIT;
                mouleuse.setEtape(next);
                chocolatierAssocie.setEtape(EtapeChocolatier.GARNIT);
                break;
            case GARNIT:
                next = EtapeMouleuse.AUCUNE;
                mouleuse.setEtape(next);
                chocolatierAssocie.setEtape(EtapeChocolatier.AUCUNE);
                mouleuse.setChocolatierUtilisantId(null);
                break;
            default:
                throw new BadCaseException("case is not handled");
        }

        return true;
    }

    public void initialiserMachineGroupe(int nombre, GroupeDeChocolatier groupe) {
        // Supprimer les mouleuses du groupe spécifié
        machineRepository.findAll().removeIf(m -> m.getGroupeDeChocolatier() == groupe);
    
        // Recréer les nouvelles mouleuses pour le groupe
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
