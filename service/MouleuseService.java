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

    public void assignerMachine(Mouleuse mouleuse, ChocolatierR chocolatier) {
        if (!estMemeGroupe(mouleuse, chocolatier.getGroupeDeChocolatier()) || chocolatier.getEtape() != EtapeChocolatier.REQUIERE_MOULEUSE) {
            System.out.println("Ce chocolatier ne peut pas utiliser cette mouleuse.");
            return;
        }

        mouleuse.setChocolatierUtilisantId(chocolatier.getId());
        mouleuse.setEtape(EtapeMouleuse.REMPLIT);
    }

    public void libererMachine(Mouleuse mouleuse) {
        if (mouleuse.getEtape() != EtapeMouleuse.FERME) {
            System.out.println("Cette mouleuse est en cours d'utilisation : " + mouleuse.getEtape().toString());
            return;
        }

        mouleuse.liberer();
    }
}
