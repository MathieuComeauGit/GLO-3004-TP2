package ressource;

import domain.Tempereuse;
import domain.enums.EtapeTempereuse;
import repository.TempereuseRepository;
import service.TempereuseService;

import static spark.Spark.post;

public class TempereuseResource extends AbstractMachineResource<EtapeTempereuse, Tempereuse, TempereuseRepository, TempereuseService> {
    public TempereuseResource(TempereuseService tempereuseService) {
        super(tempereuseService);  
    }

    protected void setPath() {
        String path = "/api/change_etape_tempereuse";
        post(path, this::changerEtapeMachine);
    }
}
