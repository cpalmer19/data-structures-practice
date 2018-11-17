package palm.util;

/**
 * A simple double-linked list written as an exercise.
 * It supports both positive and negative indices, where negative ones go
 * backwards from the end. For example, the index -1 refers to the last item.
 * 
 * @author Craig Palmer
 * @param <T> the type of item stored in the list
 */
public class LinkedList<T> {

    /**
     * Create a LinkedList with the given items.
     * The items are added in the order they appear.
     * 
     * @param items the items to add
     * @return a new LinkedList
     */
    @SafeVarargs    // is it safe? not sure
    public static <E> LinkedList<E> of(E... items) {
        LinkedList<E> list = new LinkedList<>();
        for (E item : items) {
            list.add(item);
        }
        return list;
    }
    
    private static class Node<T> {
        T value;
        Node<T> next;
        Node<T> prev;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    /**
     * Create an empty LinkedList with zero elements.
     */
    public LinkedList() {

    }

    /**
     * Get the current number of items in the list.
     * 
     * @return the current size
     */
    public int size() {
        return size;
    }

    /**
     * Check if the list is empty (contains no items)
     * 
     * @return true if there are no elements, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Get the item at the specified index.
     * 
     * @param index the index of the item to get.
     * @return the item
     * @throws IndexOutOfBoundsException if index is not within range
     */
    public T get(int index) {
        return getNode(index).value;
    }

    /**
     * Add the given item at the end of the list.
     * 
     * @param item the item to add
     */
    public void add(T item) {
        Node<T> node = new Node<>(item);

        if (size == 0) {
            head = node;
        } else {
            tail.next = node;
            node.prev = tail;
        }
        
        tail = node;
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
        // if at end, simply add
        if (index == size || index == -1) {
            add(item);
            return;
        }
        
        // allow negative indices
        if (index < 0) {
            index += 1;
        }

        Node<T> node = getNode(index);
        Node<T> newNode = new Node<>(item);

        if (node.prev != null) {
            node.prev.next = newNode;
        }
        newNode.prev = node.prev;
        node.prev = newNode;
        newNode.next = node;
        size++;
    }

    /**
     * Remove the item at the specified index.
     * 
     * @param index the index to remove the item at
     * @return the item that was just removed
     * @throws IndexOutOfBoundsException if index is not within range
     */
    public T remove(int index) {
        Node<T> node = getNode(index);
        return removeNode(node);
    }

    /**
     * Remove the specified item from the list.
     * 
     * @param item the item to remove
     * @return the item that was just removed, or null if it was not present
     */
    public T remove(T item) {
        for (Node<T> node = head; node.next != null; node = node.next) {
            if (node.value.equals(item)) {
                return removeNode(node);
            }
        }
        return null;
    }

    /**
     * Clear the list so it becomes empty.
     */
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Get the index at which the given item appears in the list.
     * 
     * @param item the item to search for
     * @return the index of the item, or -1 if not found
     */
    public int indexOf(T item) {
        int index = 0;
        Node<T> node = head;
        while (index < size) {
            if (node.value.equals(item)) {
                return index;
            }
            node = node.next;
            index++;
        }
        return -1;
    }

    /**
     * Check if the given item is present in the list.
     * 
     * @param item the item to look for
     * @return true if present, false otherwise
     */
    public boolean contains(T item) {
        return indexOf(item) != -1;
    }

    /**
     * Remove the given node from the list.
     * This decrements the size value.
     */
    private T removeNode(Node<T> node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        }

        size--;
        return node.value;
    }

    /**
     * Get the node at the specified index.
     * 
     * @throws IndexOutOfBoundsException if index is not within range
     */
    private Node<T> getNode(int index) {
        if (index < -size || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        Node<T> node;

        if (index < 0) {
            int count = -1;
            node = tail;
            while (count-- > index) {
                node = node.prev;
            }

        } else {
            int count = 0;
            node = head;
            while (count++ < index) {
                node = node.next;
            }
        }

        return node;
    }

}