package service;

import domain.ChocolatierR;
import domain.Tempereuse;
import domain.enums.EtapeChocolatier;
import domain.enums.EtapeTempereuse;
import domain.enums.GroupeDeChocolatier;
import repository.ChocolatRepository;
import repository.ChocolatierRepository;
import repository.TempereuseRepository;
import exceptions.BadCaseException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

public class TempereuseService extends AbstractMachineService<EtapeTempereuse, Tempereuse, TempereuseRepository> {

    private final Map<GroupeDeChocolatier, LinkedList<ChocolatierR>> fileAttenteParGroupe = new HashMap<>();
    private final ChocolatRepository chocolatRepository;

    public TempereuseService(TempereuseRepository tempereuseRepository, ChocolatierRepository chocolatierRepository, ChocolatRepository chocolatRepository) {
        super(tempereuseRepository, chocolatierRepository);
        fileAttenteParGroupe.put(GroupeDeChocolatier.n, new LinkedList<>());
        fileAttenteParGroupe.put(GroupeDeChocolatier.b, new LinkedList<>());
        this.chocolatRepository = chocolatRepository;
    }

    public void assignerTempereuse(Tempereuse tempereuse, UUID chocolatierId) {
        tempereuse.setChocolatierUtilisantId(chocolatierId);
        tempereuse.setEtape(EtapeTempereuse.TEMPERE_CHOCOLAT);
    }
    

    public Map<GroupeDeChocolatier, LinkedList<ChocolatierR>> getFileAttenteParGroupe() {
        return fileAttenteParGroupe;
    }

    public Boolean chocolatierInFileAttente(ChocolatierR chocolatier) {
        LinkedList<ChocolatierR> file = fileAttenteParGroupe.get(chocolatier.getGroupeDeChocolatier());
        return file != null && file.contains(chocolatier);
    }
    
    public boolean avancerEtapeParMachineId(UUID tempereuseId) throws BadCaseException {
        Tempereuse tempereuse = getMachineById(tempereuseId);
        UUID chocolatierAssocieId = tempereuse.getChocolatierUtilisantId();
        ChocolatierR chocolatierAssocie = chocolatierRepository.findById(chocolatierAssocieId);
        
        if (tempereuse == null || tempereuse.getChocolatierUtilisantId() == null)
            return false;
    
        EtapeTempereuse current = tempereuse.getEtape();
    
        switch (current) {
            case AUCUNE:
                return false;
        
            case TEMPERE_CHOCOLAT:
                retirerDeFile(chocolatierAssocie);
                chocolatRepository.consommer(chocolatierAssocie.getGroupeDeChocolatier());
                tempereuse.setEtape(EtapeTempereuse.DONNE_CHOCOLAT);
                chocolatierAssocie.setEtape(EtapeChocolatier.DONNE_CHOCOLAT);
                break;
        
            case DONNE_CHOCOLAT:
                tempereuse.setEtape(EtapeTempereuse.AUCUNE);
                chocolatierAssocie.setEtape(EtapeChocolatier.REQUIERE_MOULEUSE);
                tempereuse.setChocolatierUtilisantId(null);
                break;
        
            default:
                throw new BadCaseException("Tempereuse: état non pris en charge");
        }
    
        return true;
    }

    public void initialiserMachineGroupe(int nombre, GroupeDeChocolatier groupe) {
        machineRepository.findAll().removeIf(t -> t.getGroupeDeChocolatier() == groupe);
        for (int i = 0; i < nombre; i++) {
            UUID id = UUID.randomUUID();
            machineRepository.save(new Tempereuse(id, groupe));
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
