package service;

import domain.ChocolatierR;
import domain.Tempereuse;
import domain.enums.EtapeChocolatier;
import domain.enums.EtapeTempereuse;
import domain.enums.GroupeDeChocolatier;
import repository.TempereuseRepository;

public class TempereuseService extends MachineService<EtapeTempereuse, Tempereuse, TempereuseRepository> {
    public TempereuseService(TempereuseRepository repository) {
        super(repository);
    }

    public void creerMachine(int id, GroupeDeChocolatier groupeDeChocolatier) {
        Tempereuse tempereuse = new Tempereuse(id, groupeDeChocolatier);
        repository.save(tempereuse);
    }

    public void requeteTempereuse(Tempereuse tempereuse, ChocolatierR chocolatier) {
        if (!estMemeGroupe(tempereuse, chocolatier.getGroupeDeChocolatier()) || chocolatier.getEtape() != EtapeChocolatier.AUCUNE) {
            System.out.println("Ce chocolatier ne peut pas utiliser cette tempereuse.");
            return;
        }
    
        super.requeteMachine(tempereuse, chocolatier);
        chocolatier.setEtape(EtapeChocolatier.REQUIERE_TEMPEREUSE);
    }

    public void assignerTempereuse(Tempereuse tempereuse) {
        super.assignerMachine(tempereuse, EtapeChocolatier.REQUIERE_TEMPEREUSE, EtapeTempereuse.TEMPERE_CHOCOLAT);
    }

    public void libererMachine(Tempereuse tempereuse) {
        super.libererMachine(tempereuse, EtapeTempereuse.DONNE_CHOCOLAT);
    }
}