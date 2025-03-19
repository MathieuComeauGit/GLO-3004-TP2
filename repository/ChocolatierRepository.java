package repository;

import domain.ChocolatierR;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChocolatierRepository {
    private final List<ChocolatierR> chocolatierList = new ArrayList<>();

    public void save(ChocolatierR chocolatier) {
        chocolatierList.add(chocolatier);
    }

    public ChocolatierR findById(UUID id) {
        for (ChocolatierR c : chocolatierList) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null; 
    }

    public List<ChocolatierR> findAll() {
        return chocolatierList;
    }

    public void deleteById(UUID id) {
        chocolatierList.removeIf(c -> c.getId().equals(id));
    }

    public void clear() {
        chocolatierList.clear();
    }
}
