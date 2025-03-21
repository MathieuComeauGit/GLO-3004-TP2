package ressource;

import service.ChocolatierService;
import service.MouleuseService;
import service.TempereuseService;

import static spark.Spark.post;

public class SetupResource {

    private final ChocolatierService chocolatierService;
    private final TempereuseService tempereuseService;
    private final MouleuseService mouleuseService;

    public SetupResource(ChocolatierService chocolatierService, TempereuseService tempereuseService, MouleuseService mouleuseService) {
        this.chocolatierService = chocolatierService;
        this.tempereuseService = tempereuseService;
        this.mouleuseService = mouleuseService;

        // POST /api/reset → reset complet des données
        post("/api/reset", (req, res) -> {
            this.chocolatierService.reset();
            this.tempereuseService.reset();
            this.mouleuseService.reset();
            return "{\"message\": \"System reset avec succès\"}";
        });
    }
}
