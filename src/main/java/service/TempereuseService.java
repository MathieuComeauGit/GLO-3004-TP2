package service;

import domain.ChocolatierR;
import domain.Tempereuse;
import domain.enums.EtapeChocolatier;
import domain.enums.EtapeTempereuse;
import domain.enums.GroupeDeChocolatier;
import repository.ChocolatierRepository;
import repository.TempereuseRepository;
import exceptions.BadCaseException;

import java.util.UUID;

public class TempereuseService extends AbstractMachineService<EtapeTempereuse, Tempereuse, TempereuseRepository> {
    public TempereuseService(TempereuseRepository tempereuseRepository, ChocolatierRepository chocolatierRepository) {
        super(tempereuseRepository, chocolatierRepository);
    }

    public void assignerTempereuse(Tempereuse tempereuse) {
        super.assignerMachine(tempereuse, EtapeTempereuse.TEMPERE_CHOCOLAT);
    }

    public boolean avancerEtapeParMachineId(UUID tempereuseId) throws BadCaseException {
        Tempereuse tempereuse = getMachineById(tempereuseId);
        if (tempereuse == null || tempereuse.getChocolatierUtilisantId() == null)
            return false;
        
        EtapeTempereuse current = tempereuse.getEtape();
        EtapeTempereuse next;
        switch (current) {
            case BLOCKED:
                return false;
            case AUCUNE:
                next = EtapeTempereuse.TEMPERE_CHOCOLAT;
                tempereuse.setEtape(next);
                break;
            case TEMPERE_CHOCOLAT:
                tempereuse.setEtape(EtapeTempereuse.AUCUNE);
                UUID chocolatierAssocieId = tempereuse.getChocolatierUtilisantId();
                ChocolatierR chocolatierAssocie = chocolatierRepository.findById(chocolatierAssocieId);
                chocolatierAssocie.setEtape(EtapeChocolatier.DONNE_CHOCOLAT);
                tempereuse.setChocolatierUtilisantId(null);
                break;
            case DONNE_CHOCOLAT:
                break;
            default:
                throw new BadCaseException("case is not handled");
        }
        return true;
    }
    
    public void initialiserMachineGroupe(int nombre, GroupeDeChocolatier groupe) {
        // Supprimer les tempereuses du groupe spécifié
        machineRepository.findAll().removeIf(t -> t.getGroupeDeChocolatier() == groupe);
    
        // Recréer les nouvelles tempereuses pour le groupe
        for (int i = 0; i < nombre; i++) {
            UUID id = UUID.randomUUID();
            machineRepository.save(new Tempereuse(id, groupe));
        }
    }
    
    public EtapeTempereuse getEtapeSuivantePossible(Tempereuse tempereuse) {
        EtapeTempereuse[] etapes = EtapeTempereuse.values();
        int currentIndex = tempereuse.getEtape().ordinal();
        if (currentIndex + 1 >= etapes.length) return null;
        return etapes[currentIndex + 1];
    }    
}
