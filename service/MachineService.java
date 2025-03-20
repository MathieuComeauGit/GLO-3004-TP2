package service;

import domain.ChocolatierR;
import domain.Machine;
import domain.enums.EtapeChocolatier;
import domain.enums.GroupeDeChocolatier;
import repository.BaseRepository;

import java.util.List;
import java.util.stream.Collectors;

// T is Etape of Machine, U is Machine in Repository, V is Repository
abstract class MachineService<T, U extends Machine<T>, V extends BaseRepository<U>> {
    protected final V repository;

    public MachineService(V repository) {
        this.repository = repository;
    }

    public abstract void creerMachine(int id, GroupeDeChocolatier groupeDeChocolatier);

    public List<U> getToutesLesMachines() {
        return repository.findAll();
    }

    public U getMachineDisponible(GroupeDeChocolatier groupeDeChocolatier) {
        return repository.findAll()
                .stream()
                .filter(m -> estMemeGroupe(m, groupeDeChocolatier))
                .filter(U::estDisponible)
                .findFirst()
                .orElse(null);
    }

    public List<U> getMachinesUtilisees(GroupeDeChocolatier groupeDeChocolatier) {
        return repository.findAll()
                .stream()
                .filter(m -> estMemeGroupe(m, groupeDeChocolatier))
                .filter(m -> !m.estDisponible())
                .collect(Collectors.toList());
    }

    public void requeteMachine(U machine, ChocolatierR chocolatier) {
        machine.ajouteChocolatierListeAttente(chocolatier);
    }

    public void assignerMachine(U machine, EtapeChocolatier etapeRequete, T premiereEtape) {
        if (machine.listeAttenteVide()) {
            System.out.println("Aucun chocolatier n'a fait de requête.");
            return;
        }
        ChocolatierR chocolatier = machine.prochainChocolatier();

        if (chocolatier.getEtape() != etapeRequete) {
            System.out.println("Ce chocolatier ne peut pas utiliser cette machine.");
            return;
        }

        machine.setChocolatierUtilisantId(chocolatier.getId());
        machine.setEtape(premiereEtape);
        machine.retirerChocolatierListeAttente(chocolatier);
    }

    public void libererMachine(U machine, T derniereEtape) {
        if (machine.getEtape() != derniereEtape) {
            System.out.println("Cette machine est en cours d'utilisation : " + machine.getEtape().toString());
            return;
        }

        machine.liberer();
    }

    protected boolean estMemeGroupe(U machine, GroupeDeChocolatier groupeDeChocolatier) {
        return machine.getGroupeDeChocolatier() == groupeDeChocolatier;
    }
}
