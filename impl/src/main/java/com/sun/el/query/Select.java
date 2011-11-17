package com.sun.el.query;

import java.util.Iterator;
import javax.el.ELContext;
import javax.el.LambdaExpression;

class Select extends QueryOperator {
    @Override
    public Iterable<Object> invoke(final ELContext context,
                                   final Iterable<Object> base,
                                   final Object[] params) {
        final LambdaExpression selector = getLambda("select", params, 0);
        return new Iterable<Object>() {
            @Override
            public Iterator<Object> iterator() {
                return new SelectIterator(context, base, selector);
            }
        };
    }

    private static class SelectIterator extends BaseIterator {

        ELContext context;
        private Iterator<Object> iter;
        private LambdaExpression selector;

        public SelectIterator(ELContext context,
                              Iterable<Object>base,
                              LambdaExpression selector) {
            this.context = context;
            this.iter = base.iterator();
            this.selector = selector;
        }

        @Override
        public boolean hasNext() {
            if (iter.hasNext()) {
                current = selector.invoke(context, iter.next(), index);
                return true;
            }
            return false;
        }
    }
}
