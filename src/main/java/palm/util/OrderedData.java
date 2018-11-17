package palm.util;

/**
 * Data that is ordered and can be accessed with an index.
 * 
 * @param <T> the type of data stored
 */
public interface OrderedData<T> extends Data<T> {

    /**
     * Insert the given item at the specified index.
     * 
     * @param item the item to add
     * @param index the index to add the item
     * @throws IndexOutOfBoundsException if index is not within range
     */
    void insert(T item, int index);

    /**
     * Remove the item at the specified index.
     * 
     * @param index the index to remove the item at
     * @return the item that was just removed
     * @throws IndexOutOfBoundsException if index is not within range
     */
    T remove(int index);

    /**
     * Get the item at the specified index.
     * 
     * @param index the index of the item to get
     * @return the item
     * @throws IndexOutOfBoundsException if index is not within range
     */
    T get(int index);

    /**
     * Get the index at which the given item appears in the data.
     * 
     * @param item the item to search for
     * @return the index of the item, or -1 if not found
     */
    int indexOf(T item);
}