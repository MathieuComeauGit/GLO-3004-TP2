package service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import domain.ChocolatierR;
import domain.Mouleuse;
import domain.Tempereuse;
import domain.enums.EtapeChocolatier;
import domain.enums.EtapeTempereuse;
import domain.enums.GroupeDeChocolatier;
import repository.ChocolatRepository;
import repository.ChocolatierRepository;

import java.util.UUID;

public class ChocolatierService {
    private final ChocolatierRepository chocolatierRepository;
    private final ChocolatRepository chocolatRepository;
    private final TempereuseService tempereuseService;
    private final MouleuseService mouleuseService;

    public ChocolatierService(ChocolatierRepository chocolatierRepository,
                              TempereuseService tempereuseService,
                              MouleuseService mouleuseService, 
                              ChocolatRepository chocolatRepository) {
        this.chocolatierRepository = chocolatierRepository;
        this.tempereuseService = tempereuseService;
        this.mouleuseService = mouleuseService;
        this.chocolatRepository = chocolatRepository;
    }

    public boolean avancerEtape(UUID chocolatierId) {
        ChocolatierR chocolatier = chocolatierRepository.findById(chocolatierId);
        if (chocolatier == null) return false;

        EtapeChocolatier current = chocolatier.getEtape();
        EtapeChocolatier next = null;

        switch (current) {
            case AUCUNE:
                if (chocolatRepository.consommer()) {
                    next = EtapeChocolatier.REQUIERE_TEMPEREUSE;
                }
                else {
                    next = EtapeChocolatier.RUPTURE;
                }
                break;

            case RUPTURE:
                if (chocolatRepository.consommer()) {
                    next = EtapeChocolatier.REQUIERE_TEMPEREUSE;
                }
                break;

            case REQUIERE_TEMPEREUSE:
                Tempereuse temp = tempereuseService.getMachineDisponible(chocolatier.getGroupeDeChocolatier());
                if (temp == null) return false;
                tempereuseService.requeteMachine(temp, chocolatier);
                tempereuseService.assignerTempereuse(temp);
                next = EtapeChocolatier.TEMPERE_CHOCOLAT;
                break;

            case TEMPERE_CHOCOLAT:
                // Chocolatier passe à DONNE_CHOCOLAT si la tempereuse est à DONNE_CHOCOLAT
                Tempereuse temp1 = tempereuseService.getMachineAssociee(chocolatier.getId());
                if (temp1 == null || temp1.getEtape() != EtapeTempereuse.DONNE_CHOCOLAT) return false;
                next = EtapeChocolatier.DONNE_CHOCOLAT;
                break;
            
            case DONNE_CHOCOLAT:
                // Attendre que la tempereuse passe à AUCUNE (libérée)
                Tempereuse temp2 = tempereuseService.getMachineAssociee(chocolatier.getId());
                if (temp2 != null) return false;
                next = EtapeChocolatier.REQUIERE_MOULEUSE;
                break;
            

            case REQUIERE_MOULEUSE:
                Mouleuse m = mouleuseService.getMachineDisponible(chocolatier.getGroupeDeChocolatier());
                if (m == null) return false;
                mouleuseService.requeteMachine(m, chocolatier);
                mouleuseService.assignerMouleuse(m, chocolatier.getId());
                next = EtapeChocolatier.REMPLIT;
                break;

            case REMPLIT:
                Mouleuse moule = mouleuseService.getMachineAssociee(chocolatier.getId());
                if (moule == null || moule.getEtape() != domain.enums.EtapeMouleuse.GARNIT) return false;
                next = EtapeChocolatier.GARNIT;
                break;

            case GARNIT:
                Mouleuse moule2 = mouleuseService.getMachineAssociee(chocolatier.getId());
                if (moule2 == null || moule2.getEtape() != domain.enums.EtapeMouleuse.FERME) return false;
                next = EtapeChocolatier.FERME;
                break;

            case FERME:
                Mouleuse associee = mouleuseService.getMachineAssociee(chocolatier.getId());
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
        try {
            avancerEtape(chocolatier.getId()); // tentative d'avancement réelle
        } catch (Exception e) {
            return EtapeChocolatier.BLOCKED;
        }

        return chocolatier.getEtape();
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

        res.add("chocolatiers", chocoArr);
        res.add("tempereuses", tempService.getToutesLesMachinesCommeObjets());
        res.add("mouleuses", mouleService.getToutesLesMachinesCommeObjets());
        return res;
    }

    public void initialiserChocolatiersGroupe(int nombre, GroupeDeChocolatier groupe) {
        chocolatierRepository.findAll().removeIf(c -> c.getGroupeDeChocolatier() == groupe);
        for (int i = 0; i < nombre; i++) {
            chocolatierRepository.save(new ChocolatierR(UUID.randomUUID(), groupe));
        }
    }

    public void reset() {
        chocolatierRepository.clear();
    }
}
