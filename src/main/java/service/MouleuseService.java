package service;

import domain.ChocolatierR;
import domain.Mouleuse;
import domain.enums.EtapeChocolatier;
import domain.enums.EtapeMouleuse;
import domain.enums.GroupeDeChocolatier;
import repository.ChocolatierRepository;
import repository.MouleuseRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MouleuseService {
    private final MouleuseRepository mouleuseRepository;
    private final ChocolatierRepository chocolatierRepository;

    public MouleuseService(MouleuseRepository mouleuseRepository, ChocolatierRepository chocolatierRepository) {
        this.mouleuseRepository = mouleuseRepository;
        this.chocolatierRepository = chocolatierRepository;
    }

    public List<Mouleuse> getToutesLesMouleuses() {
        return mouleuseRepository.findAll();
    }

    public Mouleuse getMouleuseDisponible(GroupeDeChocolatier groupeDeChocolatier) {
        return mouleuseRepository.findAll()
                .stream()
                .filter(Mouleuse::estDisponible)
                .filter(m -> m.getGroupeDeChocolatier() == groupeDeChocolatier)
                .findFirst()
                .orElse(null);
    }
    

    public void assignerMouleuse(Mouleuse mouleuse, UUID chocolatierId) {
        mouleuse.setChocolatierUtilisantId(chocolatierId);
        mouleuse.setEtape(EtapeMouleuse.REMPLIT);
    }

    public void libererMouleuse(Mouleuse mouleuse) {
        mouleuse.rendreDisponible();
    }

    public List<Mouleuse> getMouleusesUtilisees() {
        return mouleuseRepository.findAll()
                .stream()
                .filter(m -> !m.estDisponible())
                .collect(Collectors.toList());
    }

    public Mouleuse getMouleuseAssociee(UUID chocolatierId) {
        return mouleuseRepository.findAll()
                .stream()
                .filter(m -> chocolatierId.equals(m.getChocolatierUtilisantId()))
                .findFirst()
                .orElse(null);
    }

    public boolean avancerEtapeParMouleuseId(UUID mouleuseId) {
        Mouleuse mouleuse = getMouleuseById(mouleuseId);
        if (mouleuse == null || mouleuse.getChocolatierUtilisantId() == null)
            return false;

        EtapeMouleuse current = mouleuse.getEtape();
        EtapeMouleuse next;
        UUID chocolatierAssocieId = mouleuse.getChocolatierUtilisantId();
        ChocolatierR chocolatierAssocie = chocolatierRepository.findById(chocolatierAssocieId);
        switch (current) {
            case AUCUNE:
                next = EtapeMouleuse.REMPLIT;
                mouleuse.setEtape(next);
                break;

            case REMPLIT:
                next = EtapeMouleuse.GARNIT;
                mouleuse.setEtape(next);
                chocolatierAssocie.setEtape(EtapeChocolatier.GARNIT_MOULE);
                break;

            case GARNIT:
                next = EtapeMouleuse.AUCUNE;
                mouleuse.setEtape(next);
                chocolatierAssocie.setEtape(EtapeChocolatier.AUCUNE);
                mouleuse.setChocolatierUtilisantId(null);
                break;

            default:
                return false;
        }

        return true;
    }

    public Mouleuse getMouleuseById(UUID id) {
        return mouleuseRepository.findById(id);
    }

    public void initialiserMouleusesGroupe(int nombre, GroupeDeChocolatier groupe) {
        // Supprimer les mouleuses du groupe spécifié
        mouleuseRepository.findAll().removeIf(m -> m.getGroupeDeChocolatier() == groupe);
    
        // Recréer les nouvelles mouleuses pour le groupe
        for (int i = 0; i < nombre; i++) {
            UUID id = UUID.randomUUID();
            mouleuseRepository.save(new Mouleuse(id, groupe));
        }
    }
    

    public EtapeMouleuse getEtapeSuivantePossible(Mouleuse mouleuse) {
        EtapeMouleuse[] etapes = EtapeMouleuse.values();
        int currentIndex = mouleuse.getEtape().ordinal();
        if (currentIndex + 1 >= etapes.length) return null;
        return etapes[currentIndex + 1];
    }

    public void reset() {
        mouleuseRepository.clear();
    }
}
