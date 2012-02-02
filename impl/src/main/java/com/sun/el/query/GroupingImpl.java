package com.sun.el.query;

/**
 * Implements javax.el.Grouping
 * We don't extend ArrayList to avoid triggering the ListELResolver when
 * access the 'key' property.
 */

import java.util.Iterator;
import java.util.ArrayList;
import javax.el.Grouping;

public class GroupingImpl<K, T> implements Grouping<K, T>{

    private K key;
    private ArrayList<T> group;

    /**
     * Constructor
     * @param key The common key for the collection.
     */
    public GroupingImpl(K key) {
        this.group = new ArrayList<T>();
        this.key = key;
    }

    public void add(T item) {
        group.add(item);
    }

    @Override
    public Iterator<T> iterator() {
        return group.iterator();
    }

    /**
     * Return the key for the collection
     * @return The key for the collection.
     */
    @Override
    public K getKey() {
        return key;
    }

    @Override
    public String toString() {
        return key.toString() + ": " + group.toString();
    }
}

