package ressource;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import service.SimulationService;
import spark.Request;
import spark.Response;

import static spark.Spark.get;
import static spark.Spark.post;

public class InitResource {
    private final SimulationService simulationService;

    public InitResource(SimulationService simulationService) {
        this.simulationService = simulationService;

        // Démarre une simulation avec paramètres séparés pour N et B
        post("/api/init_run", this::initRun);

        // Permet d’obtenir l’état actuel
        get("/api/status", this::status);
    }

    private Object initRun(Request req, Response res) {
        JsonObject json = JsonParser.parseString(req.body()).getAsJsonObject();

        int chocoN = json.get("chocolatiersN").getAsInt();
        int chocoB = json.get("chocolatiersB").getAsInt();
        int tempN = json.get("tempereusesN").getAsInt();
        int tempB = json.get("tempereusesB").getAsInt();
        int mouleN = json.get("mouleusesN").getAsInt();
        int mouleB = json.get("mouleusesB").getAsInt();

        simulationService.initSimulation(chocoN, chocoB, tempN, tempB, mouleN, mouleB);

        res.status(200);
        res.type("application/json");
        return "{\"message\": \"Simulation démarrée avec succès pour N et B\"}";
    }

    private Object status(Request req, Response res) {
        res.type("application/json");
        return simulationService.getEtat();
    }
}
