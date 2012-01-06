package com.sun.el.query;

import javax.el.ELContext;
import java.util.Iterator;

class DefaultEmpty extends QueryOperator {

    @Override
    public Iterable<Object> invoke(final ELContext context,
                         final Iterable<Object> base,
                         final Object[] params) {
        final Object defaultValue = getArgument("defaultEmpty",params,0,true);

        Iterator iter = base.iterator();
        if (iter.hasNext()) {
            return base;
        }

        return new Iterable<Object>() {
            @Override
            public Iterator<Object> iterator() {
                return new Iterator<Object>() {
                    boolean yielded = true;

                    @Override
                    public boolean hasNext() {
                        return yielded;
                    }

                    @Override
                    public Object next() {
                        yielded = false;
                        return defaultValue;
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
