package com.sun.el.query;

import java.util.Iterator;

abstract class BaseDoubleIterator implements Iterator<Object> {

    int index;

    private Iterator<Object> iter;
    private Iterator<Object> innerIter;
    private Object current;
    private boolean yielded;
    private boolean inOuter;
    Object outerItem = null;

    BaseDoubleIterator(Iterable<Object> base){
        iter = base.iterator();
        inOuter = true;
    }

    @Override
    public Object next() {
        index++;
        yielded = false;
        return current;
    }

    @Override
    public boolean hasNext() {
        while (!yielded) {
            if (inOuter) {
                if (! iter.hasNext()) {
                    break;
                }
                outerItem = iter.next();
                innerIter = doItem(outerItem);
                if (innerIter != null) {
                    inOuter = false;
                }
            } else {
                if (innerIter.hasNext()) {
                    doItem(outerItem, innerIter.next());
                } else {
                    inOuter = true;
                }
            }
        }
        return yielded;
    }

    abstract Iterator<Object> doItem(Object item);
    abstract void doItem(Object item, Object inner);

    void yield(Object current) {
        this.current = current;
        yielded = true;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
