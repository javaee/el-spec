package javax.el;

/**
 * Represents a collection of objects with a common key.
 *
 * @since EL 3.0
 */

public interface Grouping<K, T> extends Iterable<T> {

    /**
     * Return the key for the collection
     * @return The key for the collection.
     */
    public K getKey();
}
