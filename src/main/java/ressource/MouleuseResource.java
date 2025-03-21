package ressource;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import service.MouleuseService;
import spark.Request;
import spark.Response;

import static spark.Spark.post;

import java.util.UUID;


public class MouleuseResource {
    private final MouleuseService mouleuseService;


    public MouleuseResource(MouleuseService mouleuseService) {
        this.mouleuseService = mouleuseService;

        post("/api/change_etape_mouleuse", this::changerEtapeMouleuse);
    }

    private Object changerEtapeMouleuse(Request req, Response res) {
        JsonObject json = JsonParser.parseString(req.body()).getAsJsonObject();
        UUID id = UUID.fromString(json.get("id").getAsString());

        boolean success = mouleuseService.avancerEtapeParMouleuseId(id);
        if (!success) {
            res.status(400);
            return "{\"error\": \"Impossible de faire avancer la mouleuse\"}";
        }

        res.type("application/json");
        return "{\"message\": \"Étape suivante effectuée\"}";
    }
}
