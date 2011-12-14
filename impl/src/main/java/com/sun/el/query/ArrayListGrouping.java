package com.sun.el.query;

/**
 * Implements javax.el.Grouping
 */

import java.util.ArrayList;
import javax.el.Grouping;

public class ArrayListGrouping<K, T> extends ArrayList<T>
                                     implements Grouping<K, T>{

    private K key;

    /**
     * Constructor
     * @param key The common key for the collection.
     */
    public ArrayListGrouping(K key) {
        this.key = key;
    }

    /**
     * Return the key for the collection
     * @return The key for the collection.
     */
    public K getKey() {
        return key;
    }
}

