package com.sun.el.query;

import java.util.Iterator;

abstract class BaseIterator implements Iterator<Object> {

    Object current;
    int index;

    @Override
    public Object next() {
        index++;
        return current;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
