package service;

import domain.ChocolatierR;
import domain.Machine;
import domain.enums.GroupeDeChocolatier;
import repository.BaseRepository;

import java.util.List;
import java.util.stream.Collectors;

// T is Etape of Machine, U is Domain in Repository, V is Repository
abstract class MachineService<T, U extends Machine<T>, V extends BaseRepository<U>> {
    protected final V repository;

    public MachineService(V repository) {
        this.repository = repository;
    }

    public abstract void creerMachine(int id, GroupeDeChocolatier groupeDeChocolatier);
    public abstract void assignerMachine(U machine, ChocolatierR chocolatier);
    public abstract void libererMachine(U machine);

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

    protected boolean estMemeGroupe(U machine, GroupeDeChocolatier groupeDeChocolatier) {
        return machine.getGroupeDeChocolatier() == groupeDeChocolatier;
    }
}
