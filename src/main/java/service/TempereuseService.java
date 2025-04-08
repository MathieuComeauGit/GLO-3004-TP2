package service;

import domain.ChocolatierR;
import domain.Tempereuse;
import domain.enums.EtapeChocolatier;
import domain.enums.EtapeTempereuse;
import domain.enums.GroupeDeChocolatier;
import repository.ChocolatRepository;
import repository.ChocolatierRepository;
import repository.TempereuseRepository;
import exceptions.BadCaseException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

/**
 * Service responsable de la gestion des tempéreuses.
 * Coordonne les interactions entre les chocolatiers et les machines de tempérage,
 * tout en maintenant l'état des files d'attente par groupe.
 */
public class TempereuseService extends AbstractMachineService<EtapeTempereuse, Tempereuse, TempereuseRepository> {

    private final Map<GroupeDeChocolatier, LinkedList<ChocolatierR>> fileAttenteParGroupe = new HashMap<>();
    private final ChocolatRepository chocolatRepository;

    /**
     * Constructeur du service.
     *
     * @param tempereuseRepository Le dépôt des tempéreuses
     * @param chocolatierRepository Le dépôt des chocolatiers
     * @param chocolatRepository Le dépôt de stock de chocolat
     */
    public TempereuseService(TempereuseRepository tempereuseRepository,
                             ChocolatierRepository chocolatierRepository,
                             ChocolatRepository chocolatRepository) {
        super(tempereuseRepository, chocolatierRepository);
        fileAttenteParGroupe.put(GroupeDeChocolatier.n, new LinkedList<>());
        fileAttenteParGroupe.put(GroupeDeChocolatier.b, new LinkedList<>());
        this.chocolatRepository = chocolatRepository;
    }

    /**
     * Associe un chocolatier à une tempereuse pour débuter le tempérage.
     *
     * @param tempereuse La tempéreuse disponible
     * @param chocolatierId L'identifiant du chocolatier à assigner
     */
    public void assignerTempereuse(Tempereuse tempereuse, UUID chocolatierId) {
        tempereuse.setChocolatierUtilisantId(chocolatierId);
        tempereuse.setEtape(EtapeTempereuse.TEMPERE_CHOCOLAT);
    }

    /**
     * Retourne la file d'attente de chocolatiers pour chaque groupe.
     *
     * @return Une map des files d'attente groupées par groupe
     */
    public Map<GroupeDeChocolatier, LinkedList<ChocolatierR>> getFileAttenteParGroupe() {
        return fileAttenteParGroupe;
    }

    /**
     * Vérifie si un chocolatier est déjà dans la file d'attente.
     *
     * @param chocolatier Le chocolatier à vérifier
     * @return true si le chocolatier est en file, false sinon
     */
    public Boolean chocolatierInFileAttente(ChocolatierR chocolatier) {
        LinkedList<ChocolatierR> file = fileAttenteParGroupe.get(chocolatier.getGroupeDeChocolatier());
        return file != null && file.contains(chocolatier);
    }

    /**
     * Fait avancer une tempereuse selon son état courant.
     * Modifie également l’état du chocolatier associé.
     *
     * @param tempereuseId L'identifiant de la tempereuse à faire progresser
     * @return true si une étape a été franchie, false sinon
     * @throws BadCaseException Si l’état de la tempereuse n’est pas reconnu
     */
    public boolean avancerEtapeParMachineId(UUID tempereuseId) throws BadCaseException {
        Tempereuse tempereuse = getMachineById(tempereuseId);
        if (tempereuse == null || tempereuse.getChocolatierUtilisantId() == null)
            return false;

        UUID chocolatierAssocieId = tempereuse.getChocolatierUtilisantId();
        ChocolatierR chocolatierAssocie = chocolatierRepository.findById(chocolatierAssocieId);

        EtapeTempereuse current = tempereuse.getEtape();

        switch (current) {
            case AUCUNE:
                return false;

            case TEMPERE_CHOCOLAT:
                retirerDeFile(chocolatierAssocie);
                chocolatRepository.consommer(chocolatierAssocie.getGroupeDeChocolatier());
                tempereuse.setEtape(EtapeTempereuse.DONNE_CHOCOLAT);
                chocolatierAssocie.setEtape(EtapeChocolatier.DONNE_CHOCOLAT);
                break;

            case DONNE_CHOCOLAT:
                tempereuse.setEtape(EtapeTempereuse.AUCUNE);
                chocolatierAssocie.setEtape(EtapeChocolatier.REQUIERE_MOULEUSE);
                tempereuse.setChocolatierUtilisantId(null);
                break;

            default:
                throw new BadCaseException("Tempereuse: état non pris en charge");
        }

        return true;
    }

    /**
     * Supprime toutes les tempereuses d’un groupe donné, puis en crée de nouvelles.
     *
     * @param nombre Le nombre de tempereuses à créer
     * @param groupe Le groupe auquel elles appartiennent
     */
    public void initialiserMachineGroupe(int nombre, GroupeDeChocolatier groupe) {
        machineRepository.findAll().removeIf(t -> t.getGroupeDeChocolatier() == groupe);
        for (int i = 0; i < nombre; i++) {
            UUID id = UUID.randomUUID();
            machineRepository.save(new Tempereuse(id, groupe));
        }
    }

    /**
     * Ajoute un chocolatier à la file d'attente de son groupe s'il n’y est pas déjà.
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
     * Retire un chocolatier de la file d'attente de son groupe.
     *
     * @param chocolatier Le chocolatier à retirer
     */
    public void retirerDeFile(ChocolatierR chocolatier) {
        fileAttenteParGroupe.get(chocolatier.getGroupeDeChocolatier()).remove(chocolatier);
    }

    /**
     * Retourne le premier chocolatier en attente dans la file d’un groupe.
     *
     * @param groupe Le groupe ciblé
     * @return Le chocolatier en tête de file ou null si la file est vide
     */
    public ChocolatierR getPremierEnAttente(GroupeDeChocolatier groupe) {
        return fileAttenteParGroupe.get(groupe).peek();
    }

    /**
     * Réinitialise les tempereuses et vide toutes les files d’attente.
     */
    public void reset() {
        super.reset();
        fileAttenteParGroupe.values().forEach(LinkedList::clear);
    }
}
