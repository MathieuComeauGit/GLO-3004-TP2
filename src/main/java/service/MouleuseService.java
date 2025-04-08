package service;

import domain.ChocolatierR;
import domain.Mouleuse;
import domain.enums.EtapeChocolatier;
import domain.enums.EtapeMouleuse;
import domain.enums.GroupeDeChocolatier;
import exceptions.BadCaseException;
import repository.ChocolatierRepository;
import repository.MouleuseRepository;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

public class MouleuseService extends AbstractMachineService<EtapeMouleuse, Mouleuse, MouleuseRepository> {

    private final Map<GroupeDeChocolatier, LinkedList<ChocolatierR>> fileAttenteParGroupe = new HashMap<>();


    public MouleuseService(MouleuseRepository mouleuseRepository, ChocolatierRepository chocolatierRepository) {
        super(mouleuseRepository, chocolatierRepository);
        fileAttenteParGroupe.put(GroupeDeChocolatier.n, new LinkedList<>());
        fileAttenteParGroupe.put(GroupeDeChocolatier.b, new LinkedList<>());
    }

    public void assignerMouleuse(Mouleuse mouleuse, UUID chocolatierId) {
        mouleuse.setChocolatierUtilisantId(chocolatierId);
        mouleuse.setEtape(EtapeMouleuse.REMPLIT);
    }    

    public Map<GroupeDeChocolatier, LinkedList<ChocolatierR>> getFileAttenteParGroupe() {
        return fileAttenteParGroupe;
    }

    public Boolean chocolatierInFileAttente(ChocolatierR chocolatier) {
        LinkedList<ChocolatierR> file = fileAttenteParGroupe.get(chocolatier.getGroupeDeChocolatier());
        return file != null && file.contains(chocolatier);
    }

    public boolean avancerEtapeParMachineId(UUID mouleuseId) throws BadCaseException {
        Mouleuse mouleuse = getMachineById(mouleuseId);
        if (mouleuse == null || mouleuse.getChocolatierUtilisantId() == null)
            return false;
    
        UUID chocolatierAssocieId = mouleuse.getChocolatierUtilisantId();
        ChocolatierR chocolatierAssocie = chocolatierRepository.findById(chocolatierAssocieId);
    
        EtapeMouleuse current = mouleuse.getEtape();
    
        switch (current) {
            case AUCUNE:
                return false;
    
            case REMPLIT:
                retirerDeFile(chocolatierAssocie);
                mouleuse.setEtape(EtapeMouleuse.GARNIT);
                chocolatierAssocie.setEtape(EtapeChocolatier.GARNIT);
                break;
    
            case GARNIT:
                mouleuse.setEtape(EtapeMouleuse.FERME);
                chocolatierAssocie.setEtape(EtapeChocolatier.FERME);
                break;
    
            case FERME:
                mouleuse.setEtape(EtapeMouleuse.AUCUNE);
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

    public void requeteMachine(ChocolatierR chocolatier) {
        LinkedList<ChocolatierR> file = fileAttenteParGroupe.get(chocolatier.getGroupeDeChocolatier());
        if (!file.contains(chocolatier)) {
            file.add(chocolatier);
        }
    }
    
    public void retirerDeFile(ChocolatierR chocolatier) {
        fileAttenteParGroupe.get(chocolatier.getGroupeDeChocolatier()).remove(chocolatier);
    }
    
    public ChocolatierR getPremierEnAttente(GroupeDeChocolatier groupe) {
        return fileAttenteParGroupe.get(groupe).peek();
    }
    
    public void reset() {
        super.reset(); 
        fileAttenteParGroupe.values().forEach(LinkedList::clear);
    }
    
    
}
