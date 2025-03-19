package repository;

import domain.Tempereuse;

import java.util.ArrayList;
import java.util.List;

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
}
