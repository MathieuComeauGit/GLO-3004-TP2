import ressource.InitResource;
import service.SimulationService;

import static spark.Spark.*;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        // Sert les fichiers statiques (style.css, script.js, etc.)
        staticFiles.externalLocation("src/main/frontend/static");

        // Sert le HTML principal
        get("/", (req, res) -> {
            res.type("text/html");
            return Files.readString(Paths.get("src/main/frontend/templates/index.html"));
        });

        // Démarre le backend threadé
        SimulationService simulationService = new SimulationService();
        new InitResource(simulationService);

        // Serveur prêt
        System.out.println("✅ Serveur démarré sur http://localhost:4567");
    }
}
