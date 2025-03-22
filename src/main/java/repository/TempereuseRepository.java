package repository;

import java.util.UUID;
import domain.Tempereuse;

public class TempereuseRepository extends BaseRepository<Tempereuse>{

    public Tempereuse findById(UUID id) {
        for (Tempereuse tempereuse : itemsList) {
            if (tempereuse.getId().equals(id)) 
                return tempereuse;
        }
        return null;
    }
}
