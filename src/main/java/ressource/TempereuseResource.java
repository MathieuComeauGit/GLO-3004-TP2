package ressource;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import exceptions.BadCaseException;
import service.TempereuseService;
import spark.Request;
import spark.Response;

import static spark.Spark.post;

import java.util.UUID;


public class TempereuseResource {
    private final TempereuseService tempereuseService;

    public TempereuseResource(TempereuseService tempereuseService) {
        this.tempereuseService = tempereuseService;

        post("/api/change_etape_tempereuse", this::changerEtapeTempereuse);
    }

    private Object changerEtapeTempereuse(Request req, Response res) throws BadCaseException {
        JsonObject json = JsonParser.parseString(req.body()).getAsJsonObject();
        UUID id = UUID.fromString(json.get("id").getAsString());
        boolean success = tempereuseService.avancerEtapeParTempereuseId(id);
        if (!success) {
            res.status(400);
            return "{\"error\": \"Impossible d'avancer la tempereuse\"}";
        }
        res.type("application/json");
        return "{\"message\": \"Étape suivante effectuée\"}";
    }
}
