package com.sun.el.query;

import java.util.Iterator;

abstract class BaseIterator implements Iterator<Object> {

    int index;

    private Iterator<Object> iter;
    private Object current;
    private boolean yielded;
    private boolean yieldBreak;

    BaseIterator(Iterable<Object> base){
        iter = base.iterator();
    }

    @Override
    public Object next() {
        index++;
        yielded = false;
        return current;
    }

    @Override
    public boolean hasNext() {
        if (yieldBreak) {
            return false;
        }
        while ((!yielded) && iter.hasNext()) {
            doItem(iter.next());
        }
        return yielded;
    }

    abstract void doItem(Object item);

    void yield(Object current) {
        this.current = current;
        yielded = true;
    }

    void yieldBreak() {
        yieldBreak = true;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
