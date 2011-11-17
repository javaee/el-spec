package com.sun.el.query;

import java.util.Iterator;
import java.util.ArrayList;
import javax.el.ELContext;

class Reverse extends QueryOperator {
    @Override
    public Iterable<Object> invoke(final ELContext context,
                                   final Iterable<Object> base,
                                   final Object[] params) {
        return new Iterable<Object>() {
            @Override
            public Iterator<Object> iterator() {
                return new ReverseIterator(base);
            }
        };
    }

    private static class ReverseIterator extends BaseIterator {

        private ArrayList<Object> list = new ArrayList<Object>();
        private int pindex;

        public ReverseIterator(Iterable<Object> base) {
            Iterator<Object> iter = base.iterator();
            pindex = 0;
            while (iter.hasNext()) {
                list.add(iter.next());
                pindex++;
            }
        }

        @Override
        public boolean hasNext() {
            if (pindex > 0) {
                current = list.get(--pindex);
                return true;
            }
            return false;
        }
    }
}

