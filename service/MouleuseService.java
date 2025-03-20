package service;

import domain.ChocolatierR;
import domain.Mouleuse;
import domain.enums.EtapeChocolatier;
import domain.enums.EtapeMouleuse;
import domain.enums.GroupeDeChocolatier;
import repository.MouleuseRepository;

public class MouleuseService extends MachineService<EtapeMouleuse, Mouleuse, MouleuseRepository> {
    public MouleuseService(MouleuseRepository repository) {
        super(repository);
    }

    public void creerMachine(int id, GroupeDeChocolatier groupeDeChocolatier) {
        Mouleuse mouleuse = new Mouleuse(id, groupeDeChocolatier);
        repository.save(mouleuse);
    }

    public void requeteMouleuse(Mouleuse mouleuse, ChocolatierR chocolatier) {
        if (!estMemeGroupe(mouleuse, chocolatier.getGroupeDeChocolatier()) || chocolatier.getEtape() != EtapeChocolatier.DONNE_CHOCOLAT) {
            System.out.println("Ce chocolatier ne peut pas utiliser cette mouleuse.");
            return;
        }

        chocolatier.setEtape(EtapeChocolatier.REQUIERE_MOULEUSE);
        super.requeteMachine(mouleuse, chocolatier);
    }

    public void assignerMouleuse(Mouleuse mouleuse) {
        super.assignerMachine(mouleuse, EtapeChocolatier.REQUIERE_MOULEUSE, EtapeMouleuse.REMPLIT);
    }

    public void libererMouleuse(Mouleuse mouleuse) {
        super.libererMachine(mouleuse, EtapeMouleuse.FERME);
    }
}
