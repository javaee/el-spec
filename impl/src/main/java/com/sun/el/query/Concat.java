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
                return new ConcatIterator(base, second);
            }
        };
    }

    private static class ConcatIterator extends BaseIterator {

        private Iterable<Object> second;
        private Iterator<Object> iter, iter1;

        public ConcatIterator(Iterable<Object> base, Iterable<Object> second) {
            iter = iter1 = base.iterator();
            this.second = second;
        }

        @Override
        public boolean hasNext() {
            if (iter.hasNext()) {
                current = iter.next();
                return true;
            }
            if (iter == iter1) {
                iter = second.iterator();
                return hasNext();
            }
            return false;
        }
    }
}

