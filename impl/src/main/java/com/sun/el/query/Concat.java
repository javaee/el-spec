package com.sun.el.query;

import java.util.Iterator;
import javax.el.ELContext;

class Concat extends QueryOperator {
    @Override
    public Iterable<Object> invoke(final ELContext context,
                                   final Iterable<Object> base,
                                   final Object[] params) {
        final Iterable<Object> second = getIterable("concat", params, 0);
        return new Iterable<Object>() {
            @Override
            public Iterator<Object> iterator() {
                return new Iterator<Object>() {
                    private Iterator<Object> iter = base.iterator();
                    private Iterator<Object> iter2 = second.iterator();
                    private boolean yielded;
                    private Object current;

                    @Override
                    public Object next() {
                        yielded = false;
                        return current;
                    }

                    @Override
                    public boolean hasNext() {
                        if (!yielded) {
                            return false;
                        }
                        if (iter.hasNext()) {
                            yielded = true;
                            current = iter.next();
                            return true;
                        }
                        if (iter2.hasNext()) {
                            yielded = true;
                            current = iter2.next();
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }
}

