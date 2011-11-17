package com.sun.el.query;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import javax.el.ELContext;

class Distinct extends QueryOperator {
    @Override
    public Iterable<Object> invoke(final ELContext context,
                                   final Iterable<Object> base,
                                   final Object[] params) {
        return new Iterable<Object>() {
            @Override
            public Iterator<Object> iterator() {
                return new DistinctIterator(base);
            }
        };
    }

    private static class DistinctIterator extends BaseIterator {

        private Iterator<Object> iter;
        private Set<Object> set;

        public DistinctIterator(Iterable<Object> base) {
            set = new HashSet<Object>();
            iter = base.iterator();
        }

        @Override
        public boolean hasNext() {
            while (iter.hasNext()) {
                current = iter.next();
                if (set.add(current)) {
                    return true;
                }
            }
            return false;
        }
    }
}

