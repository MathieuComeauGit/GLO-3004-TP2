package service;

import domain.ChocolatierR;
import domain.enums.GroupeDeChocolatier;
import repository.ChocolatierRepository;
import repository.ChocolatRepository;

import java.util.List;
import java.util.UUID;

public class ChocolatierService {
    private final ChocolatierRepository chocolatierRepository;
    private final ChocolatRepository chocolatRepository;

    public ChocolatierService(ChocolatierRepository chocolatierRepository, ChocolatRepository chocolatRepository) {
        this.chocolatierRepository = chocolatierRepository;
        this.chocolatRepository = chocolatRepository;
    }

    public ChocolatierR creerChocolatier(GroupeDeChocolatier groupeDeChocolatier) {
        ChocolatierR chocolatier = new ChocolatierR(groupeDeChocolatier);
        chocolatierRepository.save(chocolatier);
        return chocolatier;
    }

    public ChocolatierR getChocolatierById(UUID id) {
        return chocolatierRepository.findById(id);
    }

    public List<ChocolatierR> getTousLesChocolatiers() {
        return chocolatierRepository.findAll();
    }

    public void supprimerChocolatier(UUID id) {
        chocolatierRepository.deleteById(id);
    }

    // TODO c'est avant de faire la requete qu'on a la rupture, pas pour faire garniture
    // qui est au milieu du processus.
    public void faireGarniture(UUID chocolatierId) {
        ChocolatierR chocolatier = chocolatierRepository.findById(chocolatierId);
        if (chocolatier != null) {
            boolean success = chocolatRepository.consommer(1);
            if (success) {
                // TODO: Coder ce qu'implique le fait de faire la garniture
            } 
            else {
            }
        } 
    }

    public void approvisionnerStock(int quantite) {
        chocolatRepository.approvisionner(quantite);
    }

}
