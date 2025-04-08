package service;

import domain.ChocolatierR;
import domain.Mouleuse;
import domain.enums.EtapeChocolatier;
import domain.enums.EtapeMouleuse;
import domain.enums.GroupeDeChocolatier;
import exceptions.BadCaseException;
import repository.ChocolatierRepository;
import repository.MouleuseRepository;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

/**
 * Service responsable de la gestion des mouleuses.
 * Gère les étapes de moulage des chocolats ainsi que la file d'attente par groupe.
 */
public class MouleuseService extends AbstractMachineService<EtapeMouleuse, Mouleuse, MouleuseRepository> {

    private final Map<GroupeDeChocolatier, LinkedList<ChocolatierR>> fileAttenteParGroupe = new HashMap<>();

    /**
     * Constructeur du service de mouleuses.
     *
     * @param mouleuseRepository Le dépôt de mouleuses
     * @param chocolatierRepository Le dépôt de chocolatiers
     */
    public MouleuseService(MouleuseRepository mouleuseRepository, ChocolatierRepository chocolatierRepository) {
        super(mouleuseRepository, chocolatierRepository);
        fileAttenteParGroupe.put(GroupeDeChocolatier.n, new LinkedList<>());
        fileAttenteParGroupe.put(GroupeDeChocolatier.b, new LinkedList<>());
    }

    /**
     * Associe une mouleuse à un chocolatier.
     *
     * @param mouleuse La mouleuse disponible
     * @param chocolatierId L'identifiant du chocolatier à assigner
     */
    public void assignerMouleuse(Mouleuse mouleuse, UUID chocolatierId) {
        mouleuse.setChocolatierUtilisantId(chocolatierId);
        mouleuse.setEtape(EtapeMouleuse.REMPLIT);
    }

    /**
     * Retourne la file d'attente de chocolatiers par groupe.
     *
     * @return Une map contenant les files d’attente pour chaque groupe
     */
    public Map<GroupeDeChocolatier, LinkedList<ChocolatierR>> getFileAttenteParGroupe() {
        return fileAttenteParGroupe;
    }

    /**
     * Vérifie si un chocolatier est déjà dans la file d'attente.
     *
     * @param chocolatier Le chocolatier à vérifier
     * @return true s’il est dans la file, false sinon
     */
    public Boolean chocolatierInFileAttente(ChocolatierR chocolatier) {
        LinkedList<ChocolatierR> file = fileAttenteParGroupe.get(chocolatier.getGroupeDeChocolatier());
        return file != null && file.contains(chocolatier);
    }

    /**
     * Fait progresser une mouleuse selon son état actuel et met à jour le chocolatier associé.
     *
     * @param mouleuseId L'identifiant de la mouleuse à faire avancer
     * @return true si une transition a été effectuée, false sinon
     * @throws BadCaseException Si un état de la machine n'est pas reconnu
     */
    public boolean avancerEtapeParMachineId(UUID mouleuseId) throws BadCaseException {
        Mouleuse mouleuse = getMachineById(mouleuseId);
        if (mouleuse == null || mouleuse.getChocolatierUtilisantId() == null)
            return false;

        UUID chocolatierAssocieId = mouleuse.getChocolatierUtilisantId();
        ChocolatierR chocolatierAssocie = chocolatierRepository.findById(chocolatierAssocieId);

        EtapeMouleuse current = mouleuse.getEtape();

        switch (current) {
            case AUCUNE:
                return false;

            case REMPLIT:
                retirerDeFile(chocolatierAssocie);
                mouleuse.setEtape(EtapeMouleuse.GARNIT);
                chocolatierAssocie.setEtape(EtapeChocolatier.GARNIT);
                break;

            case GARNIT:
                mouleuse.setEtape(EtapeMouleuse.FERME);
                chocolatierAssocie.setEtape(EtapeChocolatier.FERME);
                break;

            case FERME:
                mouleuse.setEtape(EtapeMouleuse.AUCUNE);
                chocolatierAssocie.setEtape(EtapeChocolatier.AUCUNE);
                mouleuse.setChocolatierUtilisantId(null);
                break;

            default:
                throw new BadCaseException("Mouleuse: état non pris en charge");
        }

        return true;
    }

    /**
     * Supprime les mouleuses d’un groupe puis en initialise un nouveau nombre.
     *
     * @param nombre Le nombre de mouleuses à créer
     * @param groupe Le groupe auquel elles appartiennent
     */
    public void initialiserMachineGroupe(int nombre, GroupeDeChocolatier groupe) {
        machineRepository.findAll().removeIf(m -> m.getGroupeDeChocolatier() == groupe);
        for (int i = 0; i < nombre; i++) {
            UUID id = UUID.randomUUID();
            machineRepository.save(new Mouleuse(id, groupe));
        }
    }

    /**
     * Ajoute un chocolatier à la file d’attente de son groupe s’il n’y est pas déjà.
     *
     * @param chocolatier Le chocolatier à ajouter
     */
    public void requeteMachine(ChocolatierR chocolatier) {
        LinkedList<ChocolatierR> file = fileAttenteParGroupe.get(chocolatier.getGroupeDeChocolatier());
        if (!file.contains(chocolatier)) {
            file.add(chocolatier);
        }
    }

    /**
     * Retire un chocolatier de la file d’attente de son groupe.
     *
     * @param chocolatier Le chocolatier à retirer
     */
    public void retirerDeFile(ChocolatierR chocolatier) {
        fileAttenteParGroupe.get(chocolatier.getGroupeDeChocolatier()).remove(chocolatier);
    }

    /**
     * Retourne le premier chocolatier dans la file d’attente pour un groupe donné.
     *
     * @param groupe Le groupe ciblé
     * @return Le chocolatier en tête de file, ou null si la file est vide
     */
    public ChocolatierR getPremierEnAttente(GroupeDeChocolatier groupe) {
        return fileAttenteParGroupe.get(groupe).peek();
    }

    /**
     * Réinitialise les mouleuses et vide toutes les files d'attente.
     */
    public void reset() {
        super.reset();
        fileAttenteParGroupe.values().forEach(LinkedList::clear);
    }
}
