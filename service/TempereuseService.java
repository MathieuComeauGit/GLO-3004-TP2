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

    public void assignerMachine(Tempereuse tempereuse, ChocolatierR chocolatier) {
        if (!estMemeGroupe(tempereuse, chocolatier.getGroupeDeChocolatier()) || chocolatier.getEtape() != EtapeChocolatier.REQUIERE_TEMPEREUSE) {
            System.out.println("Ce chocolatier ne peut pas utiliser cette tempéreuse.");
        }

        tempereuse.setChocolatierUtilisantId(chocolatier.getId());
        tempereuse.setEtape(EtapeTempereuse.TEMPERE_CHOCOLAT);

    }

    public void libererMachine(Tempereuse tempereuse) {
        if (tempereuse.getEtape() != EtapeTempereuse.DONNE_CHOCOLAT) {
            System.out.println("Cette mouleuse est en cours d'utilisation : " + tempereuse.getEtape().toString());
            return;
        }

        tempereuse.liberer();
    }
}
