package com.sun.el.query;

import java.util.Iterator;

abstract class BaseSetIterator implements Iterator<Object> {

    int index;

    private Iterable<Object> second;
    private Iterator<Object> iter;
    private Object current;
    private boolean yielded;
    private boolean yieldBreak;
    private int state = 1;

    BaseSetIterator(Iterable<Object> first, Iterable<Object> second){
        iter = first.iterator();
        this.second = second;
    }

    @Override
    public Object next() {
        yielded = false;
        return current;
    }

    @Override
    public boolean hasNext() {
        while (!yielded) {
            switch (state) {
            case 1:
                if (iter.hasNext()) {
                    doItem(iter.next());
                    break;
                }
                state = 2;
                iter = second.iterator();
            case 2:
                if (iter.hasNext()) {
                    doItem2(iter.next());
                    break;
                }
                state = 3;
                iter = getIter3();
            case 3:
                if (iter != null && iter.hasNext()) {
                    doItem3(iter.next());
                    break;
                }
                return false;
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

    Iterator<Object> getIter3() {
        return null;
    }

    void doItem3(Object item) {
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
