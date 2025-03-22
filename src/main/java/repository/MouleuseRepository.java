package repository;

import java.util.Objects;
import java.util.UUID;

import domain.Mouleuse;

public class MouleuseRepository extends BaseRepository<Mouleuse>{

    public Mouleuse findById(UUID id) {
        for (Mouleuse mouleuse : itemsList) {
            if (Objects.equals(mouleuse.getId(), id)) 
                return mouleuse;
        }
        return null;
    }
}
