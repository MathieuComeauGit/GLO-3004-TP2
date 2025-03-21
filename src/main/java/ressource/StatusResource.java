package ressource;

import service.ChocolatierService;
import service.MouleuseService;
import service.TempereuseService;

import static spark.Spark.get;

public class StatusResource {
    private final ChocolatierService chocolatierService;
    private final TempereuseService tempereuseService;
    private final MouleuseService mouleuseService;

    public StatusResource(ChocolatierService chocolatierService, TempereuseService tempereuseService, MouleuseService mouleuseService) {
        this.chocolatierService = chocolatierService;
        this.tempereuseService = tempereuseService;
        this.mouleuseService = mouleuseService;

        // === ROUTES STATUS ===
        // GET /api/status → retourne l’état complet du système
        get("/api/status", (req, res) -> getEtatComplet(req, res));
    }

    // Méthode liée à GET /api/status
    private Object getEtatComplet(spark.Request req, spark.Response res) {
        res.type("application/json");
        return chocolatierService.getEtatCompletJson(tempereuseService, mouleuseService);
    }
}
