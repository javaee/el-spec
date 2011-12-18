package com.sun.el.query;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import javax.el.ELContext;

class Union extends QueryOperator {
    @Override
    public Iterable<Object> invoke(final ELContext context,
                                   final Iterable<Object> base,
                                   final Object[] params) {
        final Iterable<Object> second = getIterable("union", params, 0);
        return new Iterable<Object>() {
            @Override
            public Iterator<Object> iterator() {
                return new BaseSetIterator(base, second) {
                    private Set<Object> set = new HashSet<Object>();

                    @Override
                    void doItem(Object item) {
                        if (set.add(item)) {
                            yield(item);
                        }
                    }

                    @Override
                    void doItem2(Object item) {
                        if (set.add(item)) {
                            yield(item);
                        }
                    }
                };
            }
        };
    }
}

