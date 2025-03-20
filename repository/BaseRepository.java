package repository;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRepository<T> {
    protected final List<T> itemsList = new ArrayList<>();

    public void save(T item) {
        itemsList.add(item);
    }

    public List<T> findAll() {
        return itemsList;
    }

    public void clear() {
        itemsList.clear();
    }
}
