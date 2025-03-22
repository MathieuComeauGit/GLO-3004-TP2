package service;

import domain.ChocolatierR;
import domain.AbstractMachine;
import domain.enums.GroupeDeChocolatier;
import exceptions.BadCaseException;
import repository.BaseRepository;
import repository.ChocolatierRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

// T is Etape of Machine, U is Machine in Repository, V is Repository
public abstract class AbstractMachineService<E extends Enum<E>, M extends AbstractMachine<E>, R extends BaseRepository<E, M>> {
    protected final R machineRepository;
    protected final ChocolatierRepository chocolatierRepository;

    public AbstractMachineService(R machineRepository, ChocolatierRepository chocolatierRepository) {
        this.machineRepository = machineRepository;
        this.chocolatierRepository = chocolatierRepository;
    }

    public abstract boolean avancerEtapeParMachineId(UUID machineId) throws BadCaseException;
    public abstract void initialiserMachineGroupe(int nombre, GroupeDeChocolatier groupe);
    public abstract E getEtapeSuivantePossible(M machine);

    /* Récupérer une ou des machines */
    public List<M> getToutesLesMachines() {
        return machineRepository.findAll();
    }

    public JsonArray getToutesLesMachinesCommeObjets() {
        JsonArray machinesArray = new JsonArray();
        for (M m : getToutesLesMachines()) {
            JsonObject obj = new JsonObject();
            obj.addProperty("id", m.getId().toString());
            obj.addProperty("etape", m.getEtape().name());
            obj.addProperty("chocolatier_id", m.getChocolatierUtilisantId() != null ? m.getChocolatierUtilisantId().toString() : null);
            obj.addProperty("groupe", m.getGroupeDeChocolatier().name().toLowerCase());
            E next = getEtapeSuivantePossible(m);
            obj.addProperty("nextStep", next != null ? next.name() : "AUCUNE");
            machinesArray.add(obj);
        }

        return machinesArray;
    }

    public M getMachineDisponible(GroupeDeChocolatier groupeDeChocolatier) {
        return machineRepository.findAll()
                .stream()
                .filter(m -> estMemeGroupe(m, groupeDeChocolatier))
                .filter(M::estDisponible)
                .findFirst()
                .orElse(null);
    }

    public List<M> getMachinesUtilisees(GroupeDeChocolatier groupeDeChocolatier) {
        return machineRepository.findAll()
                .stream()
                .filter(m -> estMemeGroupe(m, groupeDeChocolatier))
                .filter(m -> !m.estDisponible())
                .collect(Collectors.toList());
    }

    public M getMachineAssociee(UUID chocolatierId) {
        return machineRepository.findAll()
                .stream()
                .filter(m -> chocolatierId.equals(m.getChocolatierUtilisantId()))
                .findFirst()
                .orElse(null);
    }

    public M getMachineById(UUID id) {
        return machineRepository.findById(id);
    }

    /* Gestion des étapes */
    public void requeteMachine(M machine, ChocolatierR chocolatier) {
        machine.ajouteChocolatierListeAttente(chocolatier);
    }

    public void assignerMachine(M machine, E etape) {
        ChocolatierR chocolatier = machine.prochainChocolatier();
        machine.setChocolatierUtilisantId(chocolatier.getId());
        machine.setEtape(etape);
        machine.retirerChocolatierListeAttente(chocolatier);
    }

    public void libererMachine(M machine) {
        machine.rendreDisponible();
    }

    public void reset() {
        machineRepository.clear();
    }

    protected boolean estMemeGroupe(M machine, GroupeDeChocolatier groupeDeChocolatier) {
        return machine.getGroupeDeChocolatier() == groupeDeChocolatier;
    }
}