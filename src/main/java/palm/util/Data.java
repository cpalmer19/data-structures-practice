package palm.util;

/**
 * A simple collection of a type of data.
 * 
 * @param <T> the type of data stored
 */
public interface Data<T> extends Iterable<T> {

    /**
     * Get the current number of items in the data structure.
     * 
     * @return the current size
     */
    int size();

    /**
     * Check whether there are any items in this data structure.
     * @return
     */
    default boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Add the given item to this data.
     * 
     * @param item the item to add
     */
    void add(T item);

    /**
     * Remove the specified item from this data.
     * 
     * @param item the item to remove
     * @return the item that was just removed, or null if it was not present
     */
    T remove(T item);

    /**
     * Remove all the items from this data.
     */
    void clear();
}