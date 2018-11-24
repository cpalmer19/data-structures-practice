package palm.util;

import java.util.Iterator;

public class Dictionary<K, V> implements Iterable<Dictionary.Entry<K, V>> {
    
    public static class Entry<K, V> {
        final K key;
        V value;

        public static <K, V> Entry<K, V> of (K key, V value) {
            if (key == null) {
                throw new IllegalArgumentException("Dictionary.Entry does not support null keys");
            }
            return new Entry<>(key, value);
        }

        private Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K key() { return key; }

        public V value() { return value; }

        @Override
        public boolean equals(Object obj) {
            System.out.println("In Entry.equals()");
            if (obj == this) return true;
            System.out.println("Not identical objects");

            if (obj instanceof Entry<?, ?>) {
                Entry<?,?> other = (Entry<?,?>) obj;
                return key.equals(other.key);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }
    }

    //--------------------------------------------------------------

    @SafeVarargs
    public static <K, V> Dictionary<K, V> of(Entry<K, V>...entries) {
        Dictionary<K, V> dict = new Dictionary<>();
        for (Entry<K, V> entry : entries) {
            dict.add(entry);
        }
        return dict;
    }

    private int capacity;
    private Bucket<K, V>[] buckets;
    private int size;

    public Dictionary() {
        capacity = 16;
        buckets = allocateArray();
    }

    /**
     * Get the amount of entries in this Dictionary.
     * 
     * @return the current size
     */
    public int size() {
        return size;
    }

    /**
     * Add the given entry to the Dictionary.
     * 
     * @param item the entry to add
     */
    public void add(Entry<K, V> item) {
        if (item == null || item.key == null) {
            throw new IllegalArgumentException("Dictionary does not support null keys");
        }
        Entry<K, V> entry = getEntry(item.key);
        if (entry == null) {
            getBucketForAdd(item.key).add(item);
            size++;
        } else {
            entry.value = item.value;
        }
    }

    /**
     * Set the mapping for the given key to be the given value
     * 
     * @param key the key
     * @param value the new value
     */
    public void set(K key, V value) {
        Entry<K, V> entry = getEntry(key);
        if (entry == null) {
            getBucketForAdd(key).add(Entry.of(key, value));
            size++;
        } else {
            entry.value = value;
        }
    }

    /**
     * Get the value associated with the given key.
     * 
     * @param key the key
     */
    public V get(K key) {
        Entry<K, V> entry = getEntry(key);
        return entry == null ? null : entry.value;
    }

    /**
     * Check if this Dictionary contains a mapping for the given key.
     * 
     * @param key the key
     * @return true if there is an entry, false otherwise
     */
    public boolean containsKey(K key) {
        return getEntry(key) != null;
    }

    /**
     * Remove the entry for the given key.
     * 
     * @param key the key of the entry to remove
     * @return the value in the entry that was removed, or null if there was no mapping
     */
    public V remove(K key) {
        Bucket<K, V> bucket = getBucket(key);
        if (bucket == null) {
            return null;
        }
        
        Entry<K, V> entry = bucket.findEntry(key);
        if (entry == null) {
            return null;
        }

        bucket.remove(entry);
        size--;
        return entry.value;
    }

    /**
     * Clear the Dictionary
     */
    public void clear() {
        capacity = 16;
        buckets = allocateArray();
        size = 0;
    }
    
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new EntryIterator();
    }

    private class EntryIterator implements Iterator<Entry<K, V>> {
        int bucketIndex = 0;
        Iterator<Entry<K, V>> bucketIterator;

        EntryIterator() {
            bucketIterator = nextBucketIterator();
        }

        @Override
        public boolean hasNext() {
            return bucketIterator != null;
        }

        @Override
        public Entry<K, V> next() {
            Entry<K, V> entry = bucketIterator.next();
            if (! bucketIterator.hasNext()) {
                bucketIterator = nextBucketIterator();
            }
            return entry;
        }

        private Iterator<Entry<K, V>> nextBucketIterator() {
            if (bucketIndex >= capacity) return null;
            
            while (buckets[bucketIndex] == null) {
                bucketIndex++;
                if (bucketIndex >= capacity) {
                    return null;
                }
            }
            return buckets[bucketIndex++].iterator();
        }
    }

    //-----------------------------------------------------------

    @SuppressWarnings("unchecked")
    private Bucket<K, V>[] allocateArray() {
        return (Bucket<K, V>[]) new Bucket[capacity];
    }

    /**
     * Get the entry from the bucket associated with the key's hash value.
     * 
     * @param key
     * @return
     */
    private Entry<K, V> getEntry(K key) {
        if (key == null) return null;

        Bucket<K, V> bucket = getBucket(key);
        return bucket == null ? null : bucket.findEntry(key);
    }

    /**
     * Get the bucket based on the hashcode of the key.
     * Creates a new bucket if needed, so never returns null.
     * 
     * @param key
     * @return
     */
    private Bucket<K, V> getBucketForAdd(K key) {
        int index = key.hashCode() % capacity;
        if (buckets[index] == null) {
            buckets[index] = new Bucket<K, V>();
        }
        return buckets[index];
    }

    /**
     * Get the bucket based on the hashcode of the key.
     * Does not create a new bucket, no returns null if no entries exist
     * in the bucket slot.
     */
    private Bucket<K, V> getBucket(K key) {
        int index = key.hashCode() % capacity;
        return buckets[index];
    }

    //--------------------------------------------------

    private static class Bucket<K, V> implements Iterable<Entry<K, V>> {
        LinkedList<Entry<K, V>> list = new LinkedList<>();

        void add(Entry<K, V> entry) {
            list.add(entry);
        }

        void remove(Entry<K, V> entry) {
            list.remove(entry);
        }

        Entry<K, V> findEntry(K key) {
            for (Entry<K, V> entry : list) {
                if (entry.key.equals(key)) {
                    return entry;
                }
            }
            return null;
        }

        @Override
        public Iterator<Entry<K, V>> iterator() {
            return list.iterator();
        }
    }

}