package com.sun.el.query;

import java.util.Iterator;

abstract class BaseSetIterator implements Iterator<Object> {

    int index;

    private Iterator<Object> iter1, iter2;
    private Object current;
    private boolean yielded;
    private int state = 1;

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
        while (!yielded) {
            switch (state) {
            case 1:
                if (iter1.hasNext()) {
                    doItem(iter1.next());
                    break;
                }
                state = 2;
            case 2:
                if (iter2.hasNext()) {
                    doItem2(iter2.next());
                    break;
                }
                state = 3;
            case 3:
                doPost();
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

    void doPost() {
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
