package ressource;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import domain.enums.GroupeDeChocolatier;
import service.ChocolatierService;
import service.MouleuseService;
import service.TempereuseService;
import spark.Request;
import spark.Response;

import static spark.Spark.post;

public class InitResource {

    private final ChocolatierService chocolatierService;
    private final TempereuseService tempereuseService;
    private final MouleuseService mouleuseService;

    public InitResource(ChocolatierService chocolatierService,
                        TempereuseService tempereuseService,
                        MouleuseService mouleuseService) {
        this.chocolatierService = chocolatierService;
        this.tempereuseService = tempereuseService;
        this.mouleuseService = mouleuseService;

        post("/api/init_groupe", this::initGroupe);
    }

    private Object initGroupe(Request req, Response res) {
        JsonObject json = JsonParser.parseString(req.body()).getAsJsonObject();

        int nbChoco = json.get("chocolatiers").getAsInt();
        int nbTemp = json.get("tempereuses").getAsInt();
        int nbMoule = json.get("mouleuses").getAsInt();
        String groupeStr = json.get("groupe").getAsString();

        GroupeDeChocolatier groupe = GroupeDeChocolatier.valueOf(groupeStr.toLowerCase());

        chocolatierService.initialiserChocolatiersGroupe(nbChoco, groupe);
        tempereuseService.initialiserTempereusesGroupe(nbTemp, groupe);
        mouleuseService.initialiserMouleusesGroupe(nbMoule, groupe);

        res.status(200);
        return "{\"message\": \"Initialisation du groupe " + groupeStr + " terminée\"}";
    }
}
