package com.sun.el.query;

import java.util.Iterator;

abstract class BaseSetIterator implements Iterator<Object> {

    int index;

    private Iterator<Object> iter1, iter2;
    private Object current;
    private boolean yielded;
    private boolean yieldBreak;

    BaseSetIterator(Iterable<Object> first, Iterable<Object> second){
        iter1 = first.iterator();
        iter2 = second.iterator();
    }

    @Override
    public Object next() {
        yielded = false;
        return current;
    }

    @Override
    public boolean hasNext() {
        if (yieldBreak) {
            return false;
        }
        while (!yielded) {
            if (iter1.hasNext()) {
                doItem(iter1.next());
            } else if (iter2.hasNext()) {
                doItem(iter2.next());
            }
        }
        return yielded;
    }

    abstract void doItem(Object item);
    abstract void doItem2(Object item);

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
