package com.sun.el.query;

import java.util.Iterator;
import javax.el.ELContext;
import javax.el.LambdaExpression;

class TakeWhile extends QueryOperator {
    @Override
    public Iterable<Object> invoke(final ELContext context,
                                   final Iterable<Object> base,
                                   final Object[] params) {
        final LambdaExpression predicate = getLambda("takeWhile", params, 0);
        return new Iterable<Object>() {
            @Override
            public Iterator<Object> iterator() {
                return new TakeWhileIterator(context, base, predicate);
            }
        };
    }

    private static class TakeWhileIterator extends BaseIterator {

        private ELContext context;
        private Iterator<Object> iter;
        private LambdaExpression predicate;
        private boolean testedFalse = false;

        public TakeWhileIterator(ELContext context, Iterable<Object>base,
                                 LambdaExpression predicate) {
            this.context = context;
            this.iter = base.iterator();
            this.predicate = predicate;
        }

        @Override
        public boolean hasNext() {
            if (!testedFalse && iter.hasNext()) {
                current = iter.next();
                if ((Boolean)predicate.invoke(context, current, index)) {
                    return true;
                }
                else {
                    testedFalse = true;
                }
            }
            return false;
        }
    }
}
