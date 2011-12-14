package com.sun.el.query;

import java.util.Iterator;

abstract class BaseIterator implements Iterator<Object> {

    Object current;
    int index = 0;
    boolean visited;  // hasNext has been called, but next hasn't been called
    boolean visitedValue;  // if visited is true, the value to return from
                           // hasNext().

    @Override
    public Object next() {
        index++;
        visited = false;
        return current;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    boolean setVisited(boolean value) {
        visited = true;
        visitedValue = value;
        return value;
    }
}
