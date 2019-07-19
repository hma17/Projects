package bearmaps;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class MyHashMap<K, V> implements Map61B<K, V> {

    private double loadFactor;
    private int initialSize;
    private Entry<K, V>[] bins;
    private int numOfEntries;
    private HashSet keys;



    private class Entry<K, V> {
        private int length;
        private ArrayList<Node> values;

        private class Node {
            private K key;
            private V value;

            Node(K key, V val) {
                this.key = key;
                this.value = val;
            }
            private K getKey() {
                return key;
            }
            private V getValue() {
                return value;
            }
        }

        Entry() {
            values = new ArrayList<Node>();
            length = 0;
        }
        private int size() {
            return length;
        }
        private boolean isEmpty() {
            return size() == 0;
        }
        private boolean contains(K key) {
            return get(key) !=  null;
        }

        public V get(K key) {
            for (int i = 0; i < length; i++) {
                if (key.equals(values.get(i).key)) {
                    return values.get(i).value;
                }
            }
            return null;
        }

        public void add(K key, V val) {
            values.add(new Node(key, val));
            keys.add(key);
            length++;
        }
        public ArrayList<K> keys() {
            ArrayList<K> list = new ArrayList<K>();
            for (int i = 0; i < values.size(); i++) {
                list.add(values.get(i).key);
            }
            return list;
        }
    }


    public MyHashMap(int initSize) {
        initialSize = initSize;
        loadFactor = 0.75;
        keys = new HashSet<K>(16);
        bins = (Entry<K, V>[]) new Entry[initialSize];
    }

    public MyHashMap(int initSize, double loadfactor) {
        initialSize = initSize;
        loadFactor = loadfactor;
        bins = (Entry<K, V>[]) new Entry[initialSize];
        keys = new HashSet<K>();
    }

    public MyHashMap() {
        initialSize = 16;
        loadFactor = 0.75;
        bins = (Entry<K, V>[]) new Entry[initialSize];
        keys = new HashSet<K>();
    }

    @Override
    public Iterator<K> iterator() {
        return keys.iterator();
    }

    @Override
    public Set<K> keySet() {
        return keys;
    }



    private int hash(K key) {
        return (key.hashCode() & 0x7fffffff) % initialSize;
    }

    @Override
    public void clear() {
        MyHashMap x = new MyHashMap();
        this.bins = x.bins;
        this.loadFactor = x.loadFactor;
        this.initialSize = x.initialSize;
        this.numOfEntries = 0;

    }

    /** Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        return keys.contains(key);
    }
    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("put null in there boi");
        }
        int index = hash(key);
        if (bins[index] == null) {
            return null;
        }
        return bins[index].get(key);
    }

    /** Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return this.numOfEntries;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value is replaced.
     */
    private boolean getLoadFactorCalc(int numOfEnt) {
        if ((numOfEnt / bins.length) > 0.75) {
            return true;
        }
        return false;
    }
    private void resize() {
        MyHashMap temp = new MyHashMap(2 * bins.length, 0.75);
        for (int i = 0; i < this.bins.length; i++) {
            if (bins[i] != null) {
                for (K key : bins[i].keys()) {
                    temp.put(key, bins[i].get(key));
                }
            }
        }
        bins = temp.bins;
        initialSize = temp.initialSize;
        numOfEntries = temp.numOfEntries;


    }
    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("put no null");
        }
        if (getLoadFactorCalc(this.size())) {
            resize();
        }

        int i = hash(key);

        if (bins[i] != null) {
            bins[i].add(key, value);
            return;
        }
        numOfEntries++;
        Entry e = new Entry();
        e.add(key, value);
        keys.add(key);
        bins[i] = e;
    }

    /** Returns a Set view of the keys contained in this map. */

    /**
     * Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException("not here"); }

    /**
     * Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.
     */
    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException("not here");
    }
}
