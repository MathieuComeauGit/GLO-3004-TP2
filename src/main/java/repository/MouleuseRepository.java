package repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import domain.Mouleuse;

public class MouleuseRepository {
    private final List<Mouleuse> mouleuseList = new ArrayList<>();

    public void save(Mouleuse mouleuse) {
        mouleuseList.add(mouleuse);
    }

    public List<Mouleuse> findAll() {
        return mouleuseList;
    }

    public void clear() {
        mouleuseList.clear();
    }

    public Mouleuse findById(UUID id) {
        for (Mouleuse mouleuse : mouleuseList) {
            if (Objects.equals(mouleuse.getId(), id)) return mouleuse;
        }
        return null;
    }
}
