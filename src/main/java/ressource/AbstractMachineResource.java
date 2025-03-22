package ressource;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import domain.AbstractMachine;
import exceptions.BadCaseException;
import repository.BaseRepository;
import service.AbstractMachineService;
import spark.Request;
import spark.Response;

import java.util.UUID;


abstract class AbstractMachineResource<E extends Enum<E>, M extends AbstractMachine<E>, R extends BaseRepository<E, M>, S extends AbstractMachineService<E, M, R>> {
    private final S machineService;

    public AbstractMachineResource(S machineService) {
        this.machineService = machineService;
        setPath();
    }

    protected abstract void setPath();

    protected Object changerEtapeMachine(Request req, Response res) throws BadCaseException {
        JsonObject json = JsonParser.parseString(req.body()).getAsJsonObject();
        UUID id = UUID.fromString(json.get("id").getAsString());
        boolean success = machineService.avancerEtapeParMachineId(id);
        if (!success) {
            res.status(400);
            return "{\"error\": \"Impossible d'avancer la machine\"}";
        }
        res.type("application/json");
        return "{\"message\": \"Étape suivante effectuée\"}";
    }
}
