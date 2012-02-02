package com.sun.el.query;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import javax.el.ELContext;

class Except extends QueryOperator {
    @Override
    public Iterable<Object> invoke(final ELContext context,
                                   final Iterable<Object> base,
                                   final Object[] params) {
        final Iterable<Object> second = getIterable("except", params, 0);
        return new Iterable<Object>() {
            @Override
            public Iterator<Object> iterator() {
                return new BaseSetIterator(base, second) {
                    private List<Object> list = new ArrayList<Object>();

                    @Override
                    void doItem(Object item) {
                        if (!list.contains(item)) {
                            list.add(item);
                        }
                    }

                    @Override
                    void doItem2(Object item) {
                        list.remove(item);
                    }

                    @Override
                    Iterator<Object> getIter3() {
                        return list.iterator();
                    }

                    @Override
                    void doItem3(Object item) {
                        yield(item);
                    }
                };
            }
        };
    }
}

