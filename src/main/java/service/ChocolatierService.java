package service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import domain.ChocolatierR;
import domain.Mouleuse;
import domain.Tempereuse;
import domain.enums.EtapeChocolatier;
import domain.enums.GroupeDeChocolatier;
import exceptions.BadCaseException;
import repository.ChocolatRepository;
import repository.ChocolatierRepository;

import java.util.UUID;

public class ChocolatierService {
    private final ChocolatierRepository chocolatierRepository;
    private final TempereuseService tempereuseService;
    private final MouleuseService mouleuseService;
    private final ChocolatRepository chocolatRepository;

    public ChocolatierService(ChocolatierRepository chocolatierRepository,
                              TempereuseService tempereuseService,
                              MouleuseService mouleuseService,
                              ChocolatRepository chocolatRepository) {
        this.chocolatierRepository = chocolatierRepository;
        this.tempereuseService = tempereuseService;
        this.mouleuseService = mouleuseService;
        this.chocolatRepository = chocolatRepository;
    }

    public boolean avancerEtape(UUID chocolatierId) throws BadCaseException {
        ChocolatierR chocolatier = chocolatierRepository.findById(chocolatierId);
        if (chocolatier == null) return false;

        EtapeChocolatier current = chocolatier.getEtape();
        EtapeChocolatier next = null;
        Tempereuse temp = tempereuseService.getMachineDisponible(chocolatier.getGroupeDeChocolatier());
        Mouleuse m = mouleuseService.getMachineDisponible(chocolatier.getGroupeDeChocolatier());

        switch (current) {
            case AUCUNE:
                next = EtapeChocolatier.REQUIERE_TEMPEREUSE;
                break;

            case REQUIERE_TEMPEREUSE:
            
                if (!tempereuseService.chocolatierInFileAttente(chocolatier)) {
                    tempereuseService.requeteMachine(chocolatier);
                }

                if (!chocolatRepository.peutConsommer(chocolatier.getGroupeDeChocolatier())) {
                    next = EtapeChocolatier.RUPTURE;
                    break;
                }

                if (tempereuseService.getPremierEnAttente(chocolatier.getGroupeDeChocolatier()) == chocolatier) {
                    if (temp == null) {
                        return false;
                    }
                    tempereuseService.assignerTempereuse(temp, chocolatier.getId());
                    next = EtapeChocolatier.TEMPERE_CHOCOLAT;
                    break;
                }
                return false;

            case RUPTURE:
                if (chocolatRepository.peutConsommer(chocolatier.getGroupeDeChocolatier())) {
                    if (temp == null) {
                        return false;
                    }
                    tempereuseService.assignerTempereuse(temp, chocolatier.getId());
                    next = EtapeChocolatier.TEMPERE_CHOCOLAT;
                    break;
                }
                next = EtapeChocolatier.RUPTURE;
                break;

            case TEMPERE_CHOCOLAT:
            case DONNE_CHOCOLAT:
                return false;

            case REQUIERE_MOULEUSE:

                if (!mouleuseService.chocolatierInFileAttente(chocolatier)) {
                    mouleuseService.requeteMachine(chocolatier);
                }
                if (mouleuseService.getPremierEnAttente(chocolatier.getGroupeDeChocolatier()) == chocolatier) {
                    if (m == null) {
                        return false;
                    }
                    mouleuseService.assignerMouleuse(m, chocolatier.getId());
                    next = EtapeChocolatier.REMPLIT;
                    break;
                }

            case REMPLIT:
            case GARNIT:
            case FERME:
                return false;

            default:
                throw new BadCaseException("État du chocolatier inconnu.");
        }

        chocolatier.setEtape(next);
        return true;
    }

    public JsonObject getEtatCompletJson(TempereuseService tempService, MouleuseService mouleService) {
        JsonObject res = new JsonObject();

        JsonArray chocoArr = new JsonArray();
        for (ChocolatierR c : chocolatierRepository.findAll()) {
            JsonObject obj = new JsonObject();
            obj.addProperty("id", c.getId().toString());
            obj.addProperty("etape", c.getEtape().name().toUpperCase());
            obj.addProperty("groupe", c.getGroupeDeChocolatier().name().toLowerCase());
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
