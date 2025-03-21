package repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import domain.Tempereuse;

public class TempereuseRepository {
    private final List<Tempereuse> tempereuseList = new ArrayList<>();

    public void save(Tempereuse tempereuse) {
        tempereuseList.add(tempereuse);
    }

    public List<Tempereuse> findAll() {
        return tempereuseList;
    }

    public void clear() {
        tempereuseList.clear();
    }

    public Tempereuse findById(UUID id) {
        for (Tempereuse tempereuse : tempereuseList) {
            if (tempereuse.getId().equals(id)) return tempereuse;
        }
        return null;
    }
    
    
}
