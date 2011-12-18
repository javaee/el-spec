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
                return new BaseIterator(base) {
                    private Set<Object> set = new HashSet<Object>();

                    @Override
                    void doItem(Object item) {
                        if (set.add(item)) {
                            if (!secondHas(item)) {
                                yield(item);
                            }
                        }
                    }

                    private boolean secondHas(Object item) {
                        if (second instanceof Collection) {
                            return ((Collection) second).contains(item);
                        }
                        for (Object obj: second) {
                            if (obj.equals(item)) {
                                return true;
                            }
                        }
                        return false;
                    }
                };
            }
        };
    }
}

