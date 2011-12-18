package com.sun.el.query;

import java.util.Iterator;
import javax.el.ELContext;

class Skip extends QueryOperator {
    @Override
    public Iterable<Object> invoke(final ELContext context,
                                   final Iterable<Object> base,
                                   final Object[] params) {
        final int count = getInt("skip", params, 0);
        return new Iterable<Object>() {
            @Override
            public Iterator<Object> iterator() {
                return new BaseIterator(base) {
                    @Override
                    void doItem(Object item) {
                        if (index < count) {
                            return;
                        }
                        yield(item);
                    }
                };
            }
        };
    }
}
