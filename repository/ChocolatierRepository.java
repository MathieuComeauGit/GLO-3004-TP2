package repository;

import domain.ChocolatierR;

import java.util.UUID;

public class ChocolatierRepository extends BaseRepository<ChocolatierR> {
    public ChocolatierR findById(UUID id) {
        for (ChocolatierR c : itemsList) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null; 
    }

    public void deleteById(UUID id) {
        itemsList.removeIf(c -> c.getId().equals(id));
    }
}
