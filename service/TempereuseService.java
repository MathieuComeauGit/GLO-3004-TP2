package service;

import domain.Tempereuse;
import domain.enums.EtapeTempereuse;
import repository.TempereuseRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TempereuseService {
    private final TempereuseRepository tempereuseRepository;

    public TempereuseService(TempereuseRepository tempereuseRepository) {
        this.tempereuseRepository = tempereuseRepository;
    }

    public void creerTempereuse(int id) {
        Tempereuse tempereuse = new Tempereuse(id);
        tempereuseRepository.save(tempereuse);
    }

    public List<Tempereuse> getToutesLesTempereuses() {
        return tempereuseRepository.findAll();
    }

    public Tempereuse getTempereuseDisponible() {
        return tempereuseRepository.findAll()
                .stream()
                .filter(Tempereuse::estDisponible)
                .findFirst()
                .orElse(null);
    }

    public void assignerTempereuse(Tempereuse tempereuse, UUID chocolatierId) {
        tempereuse.setChocolatierUtilisantId(chocolatierId);
        tempereuse.setEtape(EtapeTempereuse.TEMPERE_CHOCOLAT);
    }

    public void libererTempereuse(Tempereuse tempereuse) {
        tempereuse.liberer();
    }

    public List<Tempereuse> getTempereusesUtilisees() {
        return tempereuseRepository.findAll()
                .stream()
                .filter(t -> !t.estDisponible())
                .collect(Collectors.toList());
    }
}
