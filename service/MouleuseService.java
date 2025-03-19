package service;

import domain.Mouleuse;
import domain.enums.EtapeMouleuse;
import repository.MouleuseRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

public class MouleuseService {
    private final MouleuseRepository mouleuseRepository;

    public MouleuseService(MouleuseRepository mouleuseRepository) {
        this.mouleuseRepository = mouleuseRepository;
    }

    public void creerMouleuse(int id) {
        Mouleuse mouleuse = new Mouleuse(id);
        mouleuseRepository.save(mouleuse);
    }

    public List<Mouleuse> getToutesLesMouleuses() {
        return mouleuseRepository.findAll();
    }

    public Mouleuse getMouleuseDisponible() {
        return mouleuseRepository.findAll()
                .stream()
                .filter(Mouleuse::estDisponible)
                .findFirst()
                .orElse(null);
    }

    public void assignerMouleuse(Mouleuse mouleuse, UUID chocolatierId) {
        mouleuse.setChocolatierUtilisantId(chocolatierId);
        mouleuse.setEtape(EtapeMouleuse.REMPLIT);
    }

    public void libererMouleuse(Mouleuse mouleuse) {
        mouleuse.liberer();
    }

    public List<Mouleuse> getMouleusesUtilisees() {
        return mouleuseRepository.findAll()
                .stream()
                .filter(m -> !m.estDisponible())
                .collect(Collectors.toList());
    }
}
