package repository;

import java.util.UUID;
import domain.ChocolatierR;

public class ChocolatierRepository extends BaseRepository<ChocolatierR>{

    public ChocolatierR findById(UUID id) {
        for (ChocolatierR c : itemsList) {
            if (c.getId().equals(id)) 
                return c;
        }
        return null; 
    }
}
