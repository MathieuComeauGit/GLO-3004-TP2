package repository;

import java.util.LinkedList;
import java.util.Objects;
import java.util.UUID;

import domain.AbstractModel;

public abstract class BaseRepository<E extends Enum<E>, M extends AbstractModel<E>> {
    protected final LinkedList<M> itemsList = new LinkedList<>();

    public void save(M item) {
        itemsList.add(item);
    }

    public LinkedList<M> findAll() {
        return itemsList;
    }

    public void clear() {
        itemsList.clear();
    }

    public M findById(UUID id) {
        for (M item : itemsList) {
            if (Objects.equals(item.getId(), id)) 
                return item;
        }
        return null;
    }
}