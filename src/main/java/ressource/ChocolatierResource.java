package ressource;

import service.ChocolatierService;
import spark.Request;
import spark.Response;

import static spark.Spark.post;

import com.google.gson.JsonParser;

import java.util.UUID;

public class ChocolatierResource {
    private final ChocolatierService chocolatierService;

    public ChocolatierResource(ChocolatierService chocolatierService) {
        this.chocolatierService = chocolatierService;

        // === ROUTES CHOCOLATIER ===
        // POST /api/change_etape → change l'étape d’un chocolatier
        post("/api/change_etape", this::changerEtape);
    }

    // Méthode liée à POST /api/change_etape
    private Object changerEtape(Request req, Response res) {
        var json = JsonParser.parseString(req.body()).getAsJsonObject();
        UUID id = UUID.fromString(json.get("id").getAsString());

        boolean success = chocolatierService.avancerEtape(id);
        if (!success) {
            res.status(400);
            return "{\"error\": \"Impossible d’avancer à l’étape suivante\"}";
        }
        return "{\"message\": \"Étape mise à jour\"}";
    }
}
