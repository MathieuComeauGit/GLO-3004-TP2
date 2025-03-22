package repository;

import java.util.LinkedList;

public abstract class BaseRepository<T> {
    protected final LinkedList<T> itemsList = new LinkedList<>();

    public void save(T item) {
        itemsList.add(item);
    }

    public LinkedList<T> findAll() {
        return itemsList;
    }

    public void clear() {
        itemsList.clear();
    }
}