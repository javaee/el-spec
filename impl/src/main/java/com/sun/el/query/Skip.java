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
                return new SkipIterator(context, base, count);
            }
        };
    }

    private static class SkipIterator extends BaseIterator {

        private ELContext context;
        private Iterator<Object> iter;
        private int count;
        private int curCount = 0;

        public SkipIterator(ELContext context, Iterable<Object>base,
                            int count) {
            this.context = context;
            this.iter = base.iterator();
            this.count = count;
        }

        @Override
        public boolean hasNext() {
            while (curCount < count  && iter.hasNext()) {
                iter.next();
                curCount++;
            }
            if (iter.hasNext()) {
                current = iter.next();
                return true;
            }
            return false;
        }
    }
}
