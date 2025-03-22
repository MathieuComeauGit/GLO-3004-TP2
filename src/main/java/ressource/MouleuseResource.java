package ressource;

import static spark.Spark.post;

import domain.Mouleuse;
import domain.enums.EtapeMouleuse;
import repository.MouleuseRepository;
import service.MouleuseService;


public class MouleuseResource extends AbstractMachineResource<EtapeMouleuse, Mouleuse, MouleuseRepository, MouleuseService> {
    public MouleuseResource(MouleuseService mouleuseService) {
        super(mouleuseService);
    }

    protected void setPath() {
        String path = "/api/change_etape_mouleuse";
        post(path, this::changerEtapeMachine);
    }
}
