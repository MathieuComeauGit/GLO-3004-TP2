package service;

import domain.ChocolatierR;
import domain.Tempereuse;
import domain.enums.EtapeChocolatier;
import domain.enums.EtapeTempereuse;
import domain.enums.GroupeDeChocolatier;
import repository.ChocolatierRepository;
import repository.TempereuseRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TempereuseService {
    private final TempereuseRepository tempereuseRepository;
    private final ChocolatierRepository chocolatierRepository;

    public TempereuseService(TempereuseRepository tempereuseRepository, ChocolatierRepository chocolatierRepository) {
        this.tempereuseRepository = tempereuseRepository;
        this.chocolatierRepository = chocolatierRepository;
    }

    public List<Tempereuse> getToutesLesTempereuses() {
        return tempereuseRepository.findAll();
    }

    public Tempereuse getTempereuseDisponible(GroupeDeChocolatier groupeDeChocolatier) {
        return tempereuseRepository.findAll()
                .stream()
                .filter(Tempereuse::estDisponible)
                .filter(t -> t.getGroupeDeChocolatier() == groupeDeChocolatier)
                .findFirst()
                .orElse(null);
    }
    

    public void assignerTempereuse(Tempereuse tempereuse, UUID chocolatierId) {
        tempereuse.setChocolatierUtilisantId(chocolatierId);
        tempereuse.setEtape(EtapeTempereuse.TEMPERE_CHOCOLAT);
    }

    public void libererTempereuse(Tempereuse tempereuse) {
        tempereuse.rendreDisponible();
    }

    public List<Tempereuse> getTempereusesUtilisees() {
        return tempereuseRepository.findAll()
                .stream()
                .filter(t -> !t.estDisponible())
                .collect(Collectors.toList());
    }

    public Tempereuse getTempereuseAssociee(UUID chocolatierId) {
        return tempereuseRepository.findAll()
                .stream()
                .filter(t -> chocolatierId.equals(t.getChocolatierUtilisantId()))
                .findFirst()
                .orElse(null);
    }

    public boolean avancerEtapeParTempereuseId(UUID tempereuseId) {
        Tempereuse tempereuse = getTempereuseById(tempereuseId);
        if (tempereuse == null || tempereuse.getChocolatierUtilisantId() == null)
            return false;
        
        EtapeTempereuse current = tempereuse.getEtape();
        EtapeTempereuse next;
        switch (current) {
            case AUCUNE:
                next = EtapeTempereuse.TEMPERE_CHOCOLAT;
                tempereuse.setEtape(next);
                break;
            case TEMPERE_CHOCOLAT:
                next = EtapeTempereuse.DONNE_CHOCOLAT;
                tempereuse.setEtape(next);
                break;
            case DONNE_CHOCOLAT:
                tempereuse.setEtape(EtapeTempereuse.AUCUNE);
                UUID chocolatierAssocieId = tempereuse.getChocolatierUtilisantId();
                ChocolatierR chocolatierAssocie = chocolatierRepository.findById(chocolatierAssocieId);
                chocolatierAssocie.setEtape(EtapeChocolatier.DONNE_CHOCOLAT);
                tempereuse.setChocolatierUtilisantId(null);
                break;

            default:
                return false;
        }
    
        return true;
    }
    
    public void initialiserTempereusesGroupe(int nombre, GroupeDeChocolatier groupe) {
        // Supprimer les tempereuses du groupe spécifié
        tempereuseRepository.findAll().removeIf(t -> t.getGroupeDeChocolatier() == groupe);
    
        // Recréer les nouvelles tempereuses pour le groupe
        for (int i = 0; i < nombre; i++) {
            UUID id = UUID.randomUUID();
            tempereuseRepository.save(new Tempereuse(id, groupe));
        }
    }
    
    
    
    public EtapeTempereuse getEtapeSuivantePossible(Tempereuse tempereuse) {
        EtapeTempereuse[] etapes = EtapeTempereuse.values();
        int currentIndex = tempereuse.getEtape().ordinal();
        if (currentIndex + 1 >= etapes.length) return null;
        return etapes[currentIndex + 1];
    }

    public Tempereuse getTempereuseById(UUID id) {
        return tempereuseRepository.findById(id);
    }
    
    public void reset() {
        tempereuseRepository.clear();
    }
    
}
