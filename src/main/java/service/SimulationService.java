package service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import domain.ChocolatierR;
import domain.Mouleuse;
import domain.Tempereuse;
import domain.enums.EtapeChocolatier;
import domain.enums.GroupeDeChocolatier;
import repository.ChocolatierRepository;
import repository.MouleuseRepository;
import repository.TempereuseRepository;
import thread.ChocolatierCountdown;
import thread.ChocolatierThread;
import thread.MouleuseThread;
import thread.TempereuseThread;

import java.util.*;

public class SimulationService {
    private final ChocolatierRepository chocolatierRepository = new ChocolatierRepository();
    private final TempereuseRepository tempereuseRepository = new TempereuseRepository();
    private final MouleuseRepository mouleuseRepository = new MouleuseRepository();

    private final TempereuseService tempereuseService = new TempereuseService(tempereuseRepository, chocolatierRepository);
    private final MouleuseService mouleuseService = new MouleuseService(mouleuseRepository, chocolatierRepository);
    private final ChocolatierService chocolatierService = new ChocolatierService(chocolatierRepository, tempereuseService, mouleuseService);

    private final List<ChocolatierThread> chocolatierThreads = new ArrayList<>();
    private final List<TempereuseThread> tempereuseThreads = new ArrayList<>();
    private final List<MouleuseThread> mouleuseThreads = new ArrayList<>();

    // Conditions pour l'ordre des chocolatiers
    private static ChocolatierCountdown chocolatierNCountdown;
    private static ChocolatierCountdown chocolatierBCountdown;

    private static GroupeDeChocolatier currentGroupeDeChocolatier;

    public static int nombreChocolatiers = 0;

    private void setPriority(GroupeDeChocolatier groupeDeChocolatier, Thread thread) {
        // 10 is Max priority, 5 if Normal priority
        int priority = groupeDeChocolatier == GroupeDeChocolatier.n ? 10 : 5;
        thread.setPriority(priority);
    }

    private static void setCurrentType(GroupeDeChocolatier groupeDeChocolatier) {
        SimulationService.currentGroupeDeChocolatier = groupeDeChocolatier;
    }

    public static void changeCurrentType() {
        GroupeDeChocolatier nextType = SimulationService.currentGroupeDeChocolatier == GroupeDeChocolatier.n ? GroupeDeChocolatier.b : GroupeDeChocolatier.n;
        setCurrentType(nextType);
    }

    public static boolean isCurrentType(GroupeDeChocolatier groupeDeChocolatier) {
        return currentGroupeDeChocolatier == groupeDeChocolatier;
    }

    public static ChocolatierCountdown getCurrentCountdown() {
        return SimulationService.currentGroupeDeChocolatier == GroupeDeChocolatier.n 
            ? SimulationService.chocolatierNCountdown 
            : SimulationService.chocolatierBCountdown;
    }

    public void initSimulation(int chocoN, int chocoB, int tempN, int tempB, int mouleN, int mouleB) {
        chocolatierRepository.clear();
        tempereuseRepository.clear();
        mouleuseRepository.clear();

        setCurrentType(GroupeDeChocolatier.n);
        nombreChocolatiers = chocoN;

        // // Initialisation des CountdownLatch
        SimulationService.chocolatierNCountdown = new ChocolatierCountdown(chocoN, GroupeDeChocolatier.n);
        SimulationService.chocolatierBCountdown = new ChocolatierCountdown(chocoB, GroupeDeChocolatier.b);
        SimulationService.chocolatierNCountdown.resetCountdown();
        SimulationService.chocolatierBCountdown.resetCountdown();

        // TEMPEREUSES N
        for (int i = 0; i < tempN; i++) {
            UUID id = UUID.randomUUID();
            Tempereuse temp = new Tempereuse(id, GroupeDeChocolatier.n);
            tempereuseRepository.save(temp);
            TempereuseThread t = new TempereuseThread(id, "temp-n-" + i, tempereuseService);
            setPriority(GroupeDeChocolatier.n, t);
            tempereuseThreads.add(t);
            t.start();
        }
    
        // TEMPEREUSES B
        for (int i = 0; i < tempB; i++) {
            UUID id = UUID.randomUUID();
            Tempereuse temp = new Tempereuse(id, GroupeDeChocolatier.b);
            tempereuseRepository.save(temp);
            TempereuseThread t = new TempereuseThread(id, "temp-b-" + i, tempereuseService);
            setPriority(GroupeDeChocolatier.b, t);
            tempereuseThreads.add(t);
            t.start();
        }
    
        // MOULEUSES N
        for (int i = 0; i < mouleN; i++) {
            UUID id = UUID.randomUUID();
            Mouleuse moule = new Mouleuse(id, GroupeDeChocolatier.n);
            mouleuseRepository.save(moule);
            MouleuseThread m = new MouleuseThread(id, "moule-n-" + i, mouleuseService);
            setPriority(GroupeDeChocolatier.n, m);
            mouleuseThreads.add(m);
            m.start();
        }
    
        // MOULEUSES B
        for (int i = 0; i < mouleB; i++) {
            UUID id = UUID.randomUUID();
            Mouleuse moule = new Mouleuse(id, GroupeDeChocolatier.b);
            mouleuseRepository.save(moule);
            MouleuseThread m = new MouleuseThread(id, "moule-b-" + i, mouleuseService);
            setPriority(GroupeDeChocolatier.b, m);
            mouleuseThreads.add(m);
            m.start();
        }
    
        // CHOCOLATIERS N
        for (int i = 0; i < chocoN; i++) {
            UUID id = UUID.randomUUID();
            chocolatierRepository.save(new ChocolatierR(id, GroupeDeChocolatier.n));
            ChocolatierThread c = new ChocolatierThread(id.toString(), i, GroupeDeChocolatier.n, chocolatierService);
            setPriority(GroupeDeChocolatier.n, c);
            chocolatierThreads.add(c);
            c.start();
        }
    
        // CHOCOLATIERS B
        for (int i = 0; i < chocoB; i++) {
            UUID id = UUID.randomUUID();
            chocolatierRepository.save(new ChocolatierR(id, GroupeDeChocolatier.b));
            ChocolatierThread c = new ChocolatierThread(id.toString(), i, GroupeDeChocolatier.b, chocolatierService);
            setPriority(GroupeDeChocolatier.b, c);
            chocolatierThreads.add(c);
            c.start();
        }
    }
    

    public JsonObject getEtat() {
        JsonObject res = new JsonObject();

        // Chocolatiers
        JsonArray chocoArray = new JsonArray();
        for (ChocolatierR c : chocolatierRepository.findAll()) {
            JsonObject o = new JsonObject();
            o.addProperty("id", c.getId().toString());
            o.addProperty("groupe", c.getGroupeDeChocolatier().name().toLowerCase());
            o.addProperty("etape", c.getEtape().name());
            EtapeChocolatier next = chocolatierService.getEtapeSuivantePossible(c);
            o.addProperty("nextStep", next != null ? next.name() : "AUCUNE");
            chocoArray.add(o);
        }

        // Tempereuses
        JsonArray tempArray = tempereuseService.getToutesLesMachinesCommeObjets();

        // Mouleuses
        JsonArray mouleArray = mouleuseService.getToutesLesMachinesCommeObjets();

        // Ajoute tout à la réponse
        res.add("chocolatiers", chocoArray);
        res.add("tempereuses", tempArray);
        res.add("mouleuses", mouleArray);

        return res;
    }
    
    public void reset() {
        chocolatierThreads.forEach(Thread::interrupt);
        tempereuseThreads.forEach(Thread::interrupt);
        mouleuseThreads.forEach(Thread::interrupt);
        chocolatierThreads.clear();
        tempereuseThreads.clear();
        mouleuseThreads.clear();
    
        chocolatierRepository.clear();
        tempereuseRepository.clear();
        mouleuseRepository.clear();
    }

}
