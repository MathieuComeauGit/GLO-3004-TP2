package service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import domain.ChocolatierR;
import domain.Tempereuse;
import domain.Mouleuse;
import domain.enums.EtapeChocolatier;
import domain.enums.GroupeDeChocolatier;
import repository.ChocolatierRepository;

import java.util.UUID;

public class ChocolatierService {
    private final ChocolatierRepository chocolatierRepository;
    private final TempereuseService tempereuseService;
    private final MouleuseService mouleuseService;

    public ChocolatierService(ChocolatierRepository chocolatierRepository,
                              TempereuseService tempereuseService,
                              MouleuseService mouleuseService) {
        this.chocolatierRepository = chocolatierRepository;
        this.tempereuseService = tempereuseService;
        this.mouleuseService = mouleuseService;
    }

    public boolean avancerEtape(UUID chocolatierId) {
        ChocolatierR chocolatier = chocolatierRepository.findById(chocolatierId);
        if (chocolatier == null) return false;
    
        EtapeChocolatier current = chocolatier.getEtape();
        EtapeChocolatier next;
    
        switch (current) {
            case AUCUNE:
                // TODO faire machine.requeteMachine -> nécessite un chocolatier et la machine spécifique (même si pas dispo)
                next = EtapeChocolatier.REQUIERE_TEMPEREUSE;
                break;
    
            case REQUIERE_TEMPEREUSE:
                Tempereuse temp = tempereuseService.getMachineDisponible(chocolatier.getGroupeDeChocolatier());
                if (temp == null) return false;
                
                // On ajoute le chocolatier à la file d'attente AVANT d'assigner
                tempereuseService.requeteMachine(temp, chocolatier);
                tempereuseService.assignerTempereuse(temp);
                
                next = EtapeChocolatier.TEMPERE_CHOCOLAT;
                break;
    
            case TEMPERE_CHOCOLAT:
                next = EtapeChocolatier.DONNE_CHOCOLAT;
                break;
    
            case DONNE_CHOCOLAT:
                next = EtapeChocolatier.REQUIERE_MOULEUSE;
                break;
    
            case REQUIERE_MOULEUSE:
                Mouleuse mDispo = mouleuseService.getMachineDisponible(chocolatier.getGroupeDeChocolatier());
                if (mDispo == null) return false;
                
                mouleuseService.requeteMachine(mDispo, chocolatier);
                mouleuseService.assignerMouleuse(mDispo, chocolatierId);
                
                next = EtapeChocolatier.REMPLIT;
                break;
    
            case REMPLIT:
                next = EtapeChocolatier.GARNIT;
                break;
    
            case GARNIT:
                next = EtapeChocolatier.FERME;
                break;
    
            case FERME:
                Mouleuse associee = mouleuseService.getMachineAssociee(chocolatierId);
                if (associee != null) mouleuseService.libererMachine(associee);
                next = EtapeChocolatier.AUCUNE;
                break;
    
            default:
                return false;
        }
    
        chocolatier.setEtape(next);
        return true;
    }
    

    public EtapeChocolatier getEtapeSuivantePossible(ChocolatierR chocolatier) {
        EtapeChocolatier current = chocolatier.getEtape();
    
        switch (current) {
            case AUCUNE:
                return EtapeChocolatier.REQUIERE_TEMPEREUSE;
    
            case REQUIERE_TEMPEREUSE:
                if (tempereuseService.getMachineDisponible(chocolatier.getGroupeDeChocolatier()) != null)
                    return EtapeChocolatier.TEMPERE_CHOCOLAT;
                return EtapeChocolatier.BLOCKED;
    
            case TEMPERE_CHOCOLAT:
                return EtapeChocolatier.BLOCKED;
    
            case DONNE_CHOCOLAT:
                return EtapeChocolatier.REQUIERE_MOULEUSE;
    
            case REQUIERE_MOULEUSE:
                if (mouleuseService.getMachineDisponible(chocolatier.getGroupeDeChocolatier()) != null)
                    return EtapeChocolatier.REMPLIT;
                return EtapeChocolatier.BLOCKED;
    
            case REMPLIT:
                return EtapeChocolatier.BLOCKED;
    
            case GARNIT:
                return EtapeChocolatier.BLOCKED;
    
            case FERME:
                return EtapeChocolatier.AUCUNE;
    
            default:
                return EtapeChocolatier.BLOCKED;
        }
    }
    
    public JsonObject getEtatCompletJson(TempereuseService tempService, MouleuseService mouleService) {
        JsonObject res = new JsonObject();

        JsonArray chocoArr = new JsonArray();
        for (ChocolatierR c : chocolatierRepository.findAll()) {
            JsonObject obj = new JsonObject();
            obj.addProperty("id", c.getId().toString());
            obj.addProperty("etape", c.getEtape().name().toUpperCase());
            obj.addProperty("groupe", c.getGroupeDeChocolatier().name().toLowerCase());
            EtapeChocolatier next = getEtapeSuivantePossible(c);
            obj.addProperty("nextStep", next != null ? next.name() : "AUCUNE");
            chocoArr.add(obj);
        }

        JsonArray tempereuseArray = tempService.getToutesLesMachinesCommeObjets();
        JsonArray mouleusesArray = mouleService.getToutesLesMachinesCommeObjets();

        res.add("chocolatiers", chocoArr);
        res.add("tempereuses", tempereuseArray);
        res.add("mouleuses", mouleusesArray);
        return res;
    }

    public void initialiserChocolatiersGroupe(int nombre, GroupeDeChocolatier groupe) {
        // Supprime tous les chocolatiers du groupe passé en paramètre
        chocolatierRepository.findAll().removeIf(c -> c.getGroupeDeChocolatier() == groupe);
    
        // Crée les nouveaux chocolatiers
        for (int i = 0; i < nombre; i++) {
            UUID id = UUID.randomUUID();
            ChocolatierR chocolatier = new ChocolatierR(id, groupe);
            chocolatierRepository.save(chocolatier);
        }
    }
    
    public void reset() {
        chocolatierRepository.clear();
    }
}
