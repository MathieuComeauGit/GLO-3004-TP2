package repository;

import domain.Mouleuse;

import java.util.ArrayList;
import java.util.List;

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
}
