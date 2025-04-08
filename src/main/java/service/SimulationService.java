package service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import domain.ChocolatierR;
import domain.Mouleuse;
import domain.Tempereuse;
import domain.enums.EtapeChocolatier;
import domain.enums.GroupeDeChocolatier;
import repository.ChocolatRepository;
import repository.ChocolatierRepository;
import repository.MouleuseRepository;
import repository.TempereuseRepository;
import thread.ApprovisionnementThread;
import thread.ChocolatierThread;
import thread.MouleuseThread;
import thread.TempereuseThread;

import java.util.*;

public class SimulationService {

    final ChocolatierRepository chocolatierRepository = new ChocolatierRepository();
    final TempereuseRepository tempereuseRepository = new TempereuseRepository();
    final MouleuseRepository mouleuseRepository = new MouleuseRepository();
    final ChocolatRepository chocolatRepository = new ChocolatRepository();

    final TempereuseService tempereuseService = new TempereuseService(tempereuseRepository, chocolatierRepository, chocolatRepository);
    final MouleuseService mouleuseService = new MouleuseService(mouleuseRepository, chocolatierRepository);
    final ChocolatierService chocolatierService = new ChocolatierService(chocolatierRepository, tempereuseService, mouleuseService, chocolatRepository);

    final List<ChocolatierThread> chocolatierThreads = new ArrayList<>();
    final List<TempereuseThread> tempereuseThreads = new ArrayList<>();
    final List<MouleuseThread> mouleuseThreads = new ArrayList<>();
    ApprovisionnementThread approvisionnementThread;

    public void initSimulation(int chocoN, int chocoB, int tempN, int tempB, int mouleN, int mouleB) {

        chocolatierRepository.clear();
        tempereuseRepository.clear();
        mouleuseRepository.clear();
        chocolatRepository.clear();

        chocolatRepository.setQuantiteN(chocoN);
        chocolatRepository.setQuantiteB(chocoB);

        // TEMPEREUSES N
        for (int i = 0; i < tempN; i++) {
            UUID id = UUID.randomUUID();
            Tempereuse temp = new Tempereuse(id, GroupeDeChocolatier.n);
            tempereuseRepository.save(temp);
            TempereuseThread t = new TempereuseThread(id, "temp-n-" + i, tempereuseService);
            tempereuseThreads.add(t);
            t.start();
        }

        // TEMPEREUSES B
        for (int i = 0; i < tempB; i++) {
            UUID id = UUID.randomUUID();
            Tempereuse temp = new Tempereuse(id, GroupeDeChocolatier.b);
            tempereuseRepository.save(temp);
            TempereuseThread t = new TempereuseThread(id, "temp-b-" + i, tempereuseService);
            tempereuseThreads.add(t);
            t.start();
        }

        // MOULEUSES N
        for (int i = 0; i < mouleN; i++) {
            UUID id = UUID.randomUUID();
            Mouleuse moule = new Mouleuse(id, GroupeDeChocolatier.n);
            mouleuseRepository.save(moule);
            MouleuseThread m = new MouleuseThread(id, "moule-n-" + i, mouleuseService);
            mouleuseThreads.add(m);
            m.start();
        }

        // MOULEUSES B
        for (int i = 0; i < mouleB; i++) {
            UUID id = UUID.randomUUID();
            Mouleuse moule = new Mouleuse(id, GroupeDeChocolatier.b);
            mouleuseRepository.save(moule);
            MouleuseThread m = new MouleuseThread(id, "moule-b-" + i, mouleuseService);
            mouleuseThreads.add(m);
            m.start();
        }

        // CHOCOLATIERS N
        for (int i = 0; i < chocoN; i++) {
            UUID id = UUID.randomUUID();
            chocolatierRepository.save(new ChocolatierR(id, GroupeDeChocolatier.n));
            ChocolatierThread c = new ChocolatierThread(id.toString(), GroupeDeChocolatier.n, chocolatierService);
            chocolatierThreads.add(c);
            c.start();
        }

        // CHOCOLATIERS B
        for (int i = 0; i < chocoB; i++) {
            UUID id = UUID.randomUUID();
            chocolatierRepository.save(new ChocolatierR(id, GroupeDeChocolatier.b));
            ChocolatierThread c = new ChocolatierThread(id.toString(), GroupeDeChocolatier.b, chocolatierService);
            chocolatierThreads.add(c);
            c.start();
        }

        // APPROVISIONNEMENT
        approvisionnementThread = new ApprovisionnementThread(chocolatierRepository, chocolatRepository);
        approvisionnementThread.start();
    }

    public JsonObject getEtat() {
        JsonObject res = new JsonObject();

        JsonArray chocoArray = new JsonArray();
        for (ChocolatierR c : chocolatierRepository.findAll()) {
            JsonObject o = new JsonObject();
            o.addProperty("id", c.getId().toString());
            o.addProperty("groupe", c.getGroupeDeChocolatier().name().toLowerCase());
            o.addProperty("etape", c.getEtape().name());

            LinkedList<ChocolatierR> fileTemp = tempereuseService.getFileAttenteParGroupe()
                    .getOrDefault(c.getGroupeDeChocolatier(), new LinkedList<>());
            LinkedList<ChocolatierR> fileMoule = mouleuseService.getFileAttenteParGroupe()
                    .getOrDefault(c.getGroupeDeChocolatier(), new LinkedList<>());

            int posTemp = fileTemp.indexOf(c);
            int posMoule = fileMoule.indexOf(c);
            o.addProperty("position_tempereuse", posTemp >= 0 ? posTemp + 1 : -1);
            o.addProperty("position_mouleuse", posMoule >= 0 ? posMoule + 1 : -1);

            chocoArray.add(o);
        }

        res.add("chocolatiers", chocoArray);
        res.add("tempereuses", tempereuseService.getToutesLesMachinesCommeObjets());
        res.add("mouleuses", mouleuseService.getToutesLesMachinesCommeObjets());

        JsonObject stock = new JsonObject();
        stock.addProperty("n", chocolatRepository.getQuantite(GroupeDeChocolatier.n));
        stock.addProperty("b", chocolatRepository.getQuantite(GroupeDeChocolatier.b));
        res.add("stock", stock);

        return res;
    }

    public void reset() {
        chocolatierThreads.forEach(Thread::interrupt);
        tempereuseThreads.forEach(Thread::interrupt);
        mouleuseThreads.forEach(Thread::interrupt);
        chocolatierThreads.clear();
        tempereuseThreads.clear();
        mouleuseThreads.clear();

        if (approvisionnementThread != null) {
            approvisionnementThread.interrupt();
        }

        chocolatierRepository.clear();
        chocolatRepository.clear();
        tempereuseService.reset();
        mouleuseService.reset();
    }
}
