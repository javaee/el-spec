package com.sun.el.query;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;

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
                    private Set<Object> set = new HashSet<Object>();
                    private Iterator iter = set.iterator();

                    @Override
                    void doItem(Object item) {
                        set.add(item);
                    }

                    @Override
                    void doItem2(Object item) {
                        set.remove(item);
                    }

                    @Override
                    void doPost() {
                        if (iter.hasNext()) {
                            yield(iter.next());
                        }
                    }
                };
            }
        };
    }
}

