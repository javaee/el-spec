package com.sun.el.query;

import java.util.Iterator;
import javax.el.ELContext;

class Take extends QueryOperator {
    @Override
    public Iterable<Object> invoke(final ELContext context,
                                   final Iterable<Object> base,
                                   final Object[] params) {
        final int count = getInt("take", params, 0);
        return new Iterable<Object>() {
            @Override
            public Iterator<Object> iterator() {
                return new TakeIterator(context, base, count);
            }
        };
    }

    private static class TakeIterator extends BaseIterator {

        private ELContext context;
        private Iterator<Object> iter;
        private int count;
        private int curCount = 0;

        public TakeIterator(ELContext context, Iterable<Object>base,
                             int count) {
            this.context = context;
            this.iter = base.iterator();
            this.count = count;
        }

        @Override
        public boolean hasNext() {
            if (curCount < count  && iter.hasNext()) {
                current = iter.next();
                curCount++;
                return true;
            }
            return false;
        }
    }
}
