package palm.util;

import java.util.Iterator;

/**
 * A simple dynamically-sized array.
 * 
 * @param <T> the type of data in the array
 */
public class DynamicArray<T> implements Iterable<T> {

    /**
     * Create an DynamicArray with the specified elements.
     * Elements are stored in the order they appear.
     * 
     * @param elements the elements to add
     */
    @SafeVarargs
    public static <E> DynamicArray<E> of(E... elements) {
        DynamicArray<E> arr = new DynamicArray<>();
        for (E elem : elements) {
            arr.add(elem);
        }
        return arr;
    }

    /**
     * Factor for growing and shrinking the capacity
     */
    private static int SCALE_FACTOR = 2;

    /**
     * Ratio of the capacity/size to trigger shrinking
     */
    private static int SHRINK_RATIO = 4;

    private int capacity;
    private int size;
    private T[] elements;

    /**
     * Create an empty DynamicArray.
     */
    public DynamicArray() {
        capacity = 5;
        elements = allocateArray();
        size = 0;
    }

    /**
     * Get the number of items currently in the array.
     */
    public int size() {
        return size;
    }

    /**
     * Add the given element to the end of the array.
     * @param item the element to add
     */
    public void add(T item) {
        ensureCapacity();
        elements[size] = item;
        size++;
    }

    /**
     * Insert the given item at the specified index.
     * The index must be in range 0 < index <= size.
     * 
     * @param item the item to add
     * @param index the index to add the item
     * @throws IndexOutOfBoundsException if index is not within range
     */
    public void insert(T item, int index) {
        index = absoluteIndexForInsert(index);
        checkIndexForInsert(index);
        ensureCapacity();

        shiftElementsUp(index);
        elements[index] = item;
        size++;
    }

    /**
     * Remove the specified item from the array.
     * 
     * @param item the item to remove
     * @return the item removed, or null if it was not present
     */
    public T remove(T item) {
        int index = indexOf(item);
        return (index == -1) ? null : remove(index);
    }

    /**
     * Remove the item at the specified index.
     * 
     * @param index the index to remove the item from
     * @return the item removed
     */
    public T remove(int index) {
        index = absoluteIndex(index);
        T item = getElement(index);
        shiftElementsDown(index);
        size--;

        shrinkIfNeeded();
        return item;
    }

    /**
     * Clear all the elements from this array.
     */
    public void clear() {
        size = 0;
    }

    /**
     * Get the item at the specified index.
     * 
     * @param index the index to get the item from
     * @return the item
     * @throws IndexOutOfBoundsException if index is not within range
     */
    public T get(int index) {
        index = absoluteIndex(index);
        return getElement(index);
    }

   /**
    * Find the index that the given item appears at.
    * 
    * @param item the item to find
    * @return the index, or -1 if not found
    */
    public int indexOf(T item) {
		for (int i = 0; i < size; i++) {
            if (item.equals(elements[i])) {
                return i;
            }
        }
        return -1;
	}

    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<T> {
        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public T next() {
            return elements[index++];
        }
    }

    //----------------

    @SuppressWarnings("unchecked")
    private T[] allocateArray() {
        return (T[]) new Object[capacity];
    }

    private void ensureCapacity() {
        if (size == capacity) {
            capacity = capacity * SCALE_FACTOR;
            T[] newArr = allocateArray();
            transferElements(newArr);
        }
    }

    private void transferElements(T[] newArr) {
        if (newArr.length <= size) {
            throw new IllegalStateException();
        }

        for (int i = 0; i < size; i++) {
            newArr[i] = elements[i];
        }

        elements = newArr;
    }

    private void shrinkIfNeeded() {
        if ((capacity / size) >= SHRINK_RATIO) {
            capacity = capacity / SCALE_FACTOR;
        }
    }

    private void shiftElementsUp(int start) {
        for (int i = size-1; i >= start; i--) {
            elements[i+1] = elements[i];
        }
    }

    private void shiftElementsDown(int start) {
        for (int i = start; i < size-1; i++) {
            elements[i] = elements[i+1];
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
    }

    private void checkIndexForInsert(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
    }

    private int absoluteIndex(int index) {
        return index < 0 ? size + index : index;
    }

    private int absoluteIndexForInsert(int index) {
        return index < 0 ? size + index + 1 : index;
    }

    private T getElement(int index) {
        checkIndex(index);
        return elements[index];
    }

    /**
     * (Package private only for testing)
     * 
     * @return the capacity
     */
    int getCapacity() {
        return capacity;
    }

}