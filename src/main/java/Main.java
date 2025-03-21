import repository.*;
import ressource.*;
import service.*;

import static spark.Spark.*;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        staticFiles.externalLocation("src/main/frontend/static");

        get("/", (req, res) -> {
            res.type("text/html");
            return Files.readString(Paths.get("src/main/frontend/templates/index.html"));
        });

        ChocolatierRepository chocoRepo = new ChocolatierRepository();
        TempereuseRepository tempRepo = new TempereuseRepository();
        MouleuseRepository mouleRepo = new MouleuseRepository();

        TempereuseService tempService = new TempereuseService(tempRepo, chocoRepo);
        MouleuseService mouleService = new MouleuseService(mouleRepo, chocoRepo);
        ChocolatierService chocoService = new ChocolatierService(chocoRepo, tempService, mouleService);

        new InitResource(chocoService, tempService, mouleService); 
        new SetupResource(chocoService, tempService, mouleService);
        new StatusResource(chocoService, tempService, mouleService);

        new ChocolatierResource(chocoService);
        new TempereuseResource(tempService);
        new MouleuseResource(mouleService);


    }
}
