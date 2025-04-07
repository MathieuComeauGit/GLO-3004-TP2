package repository;

import java.util.LinkedList;
import java.util.Objects;
import java.util.UUID;

import domain.AbstractModel;

public abstract class BaseRepository<E extends Enum<E>, M extends AbstractModel<E>> {
    protected final LinkedList<M> itemsList = new LinkedList<>();
    protected int numberOfItems = 0;

    public void save(M item) {
        itemsList.add(item);
        numberOfItems++;
    }

    public LinkedList<M> findAll() {
        return itemsList;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void clear() {
        itemsList.clear();
        numberOfItems = 0;
    }

    public M findById(UUID id) {
        for (M item : itemsList) {
            if (Objects.equals(item.getId(), id)) 
                return item;
        }
        return null;
    }
}