package service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import domain.ChocolatierR;
import domain.Tempereuse;
import domain.Mouleuse;
import domain.enums.EtapeChocolatier;
import domain.enums.EtapeMouleuse;
import domain.enums.EtapeTempereuse;
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
                next = EtapeChocolatier.REQUIS_TEMPEREUSE;
                break;
    
            case REQUIS_TEMPEREUSE:
                Tempereuse temp = tempereuseService.getTempereuseDisponible(chocolatier.getProvenance());
                if (temp == null) return false;
                tempereuseService.assignerTempereuse(temp, chocolatierId);
                next = EtapeChocolatier.TEMPERE_CHOCOLAT;
                break;
    
            case TEMPERE_CHOCOLAT:
                next = EtapeChocolatier.DONNE_CHOCOLAT;
                break;
    
            case DONNE_CHOCOLAT:
                next = EtapeChocolatier.REQUIS_MOULEUSE;
                break;
    
            case REQUIS_MOULEUSE:
                Mouleuse mDispo = mouleuseService.getMouleuseDisponible(chocolatier.getProvenance());
                if (mDispo == null) return false;
                mouleuseService.assignerMouleuse(mDispo, chocolatierId);
                next = EtapeChocolatier.REMPLIT_MOULE;
                break;
    
            case REMPLIT_MOULE:
                next = EtapeChocolatier.GARNIT_MOULE;
                break;
    
            case GARNIT_MOULE:
                next = EtapeChocolatier.FERME_MOULE;
                break;
    
            case FERME_MOULE:
                Mouleuse associee = mouleuseService.getMouleuseAssociee(chocolatierId);
                if (associee != null) mouleuseService.libererMouleuse(associee);
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
                return EtapeChocolatier.REQUIS_TEMPEREUSE;
    
            case REQUIS_TEMPEREUSE:
                if (tempereuseService.getTempereuseDisponible(chocolatier.getProvenance()) != null)
                    return EtapeChocolatier.TEMPERE_CHOCOLAT;
                return EtapeChocolatier.BLOCKED;
    
            case TEMPERE_CHOCOLAT:
                return EtapeChocolatier.BLOCKED;
    
            case DONNE_CHOCOLAT:
                return EtapeChocolatier.REQUIS_MOULEUSE;
    
            case REQUIS_MOULEUSE:
                if (mouleuseService.getMouleuseDisponible(chocolatier.getProvenance()) != null)
                    return EtapeChocolatier.REMPLIT_MOULE;
                return EtapeChocolatier.BLOCKED;
    
            case REMPLIT_MOULE:
                return EtapeChocolatier.BLOCKED;
    
            case GARNIT_MOULE:
                return EtapeChocolatier.BLOCKED;
    
            case FERME_MOULE:
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
            obj.addProperty("groupe", c.getProvenance().name().toLowerCase());
            EtapeChocolatier next = getEtapeSuivantePossible(c);
            obj.addProperty("nextStep", next != null ? next.name() : "AUCUNE");
            chocoArr.add(obj);
        }

        JsonArray tempArr = new JsonArray();
        for (Tempereuse t : tempService.getToutesLesTempereuses()) {
            JsonObject obj = new JsonObject();
            obj.addProperty("id", t.getId().toString());
            obj.addProperty("etape", t.getEtape().name());
            obj.addProperty("chocolatier_id", t.getChocolatierUtilisantId() != null ? t.getChocolatierUtilisantId().toString() : null);
            obj.addProperty("groupe", t.getGroupeDeChocolatier().name().toLowerCase());
            EtapeTempereuse next = tempService.getEtapeSuivantePossible(t);
            obj.addProperty("nextStep", next != null ? next.name() : "AUCUNE");
            tempArr.add(obj);
        }

        JsonArray mouleArr = new JsonArray();
        for (Mouleuse m : mouleService.getToutesLesMouleuses()) {
            JsonObject obj = new JsonObject();
            obj.addProperty("id", m.getId().toString());
            obj.addProperty("etape", m.getEtape().name());
            obj.addProperty("chocolatier_id", m.getChocolatierUtilisantId() != null ? m.getChocolatierUtilisantId().toString() : null);
            obj.addProperty("groupe", m.getGroupeDeChocolatier().name().toLowerCase());
            EtapeMouleuse next = mouleService.getEtapeSuivantePossible(m);
            obj.addProperty("nextStep", next != null ? next.name() : "AUCUNE");
            mouleArr.add(obj);
        }

        res.add("chocolatiers", chocoArr);
        res.add("tempereuses", tempArr);
        res.add("mouleuses", mouleArr);
        return res;
    }

    public void initialiserChocolatiersGroupe(int nombre, GroupeDeChocolatier groupe) {
        // Supprime tous les chocolatiers du groupe passé en paramètre
        chocolatierRepository.findAll().removeIf(c -> c.getProvenance() == groupe);
    
        // Crée les nouveaux chocolatiers
        for (int i = 0; i < nombre; i++) {
            chocolatierRepository.save(new ChocolatierR(groupe));
        }
    }
    

    public void reset() {
        chocolatierRepository.clear();
    }
}
