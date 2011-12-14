package com.sun.el.query;

import java.util.Iterator;
import javax.el.ELContext;
import javax.el.LambdaExpression;

class SkipWhile extends QueryOperator {
    @Override
    public Iterable<Object> invoke(final ELContext context,
                                   final Iterable<Object> base,
                                   final Object[] params) {
        final LambdaExpression predicate = getLambda("takeWhile", params, 0);
        return new Iterable<Object>() {
            @Override
            public Iterator<Object> iterator() {
                return new SkipWhileIterator(context, base, predicate);
            }
        };
    }

    private static class SkipWhileIterator extends BaseIterator {

        private ELContext context;
        private Iterator<Object> iter;
        private LambdaExpression predicate;
        private boolean testedFalse = false;

        public SkipWhileIterator(ELContext context, Iterable<Object>base,
                            LambdaExpression predicate) {
            this.context = context;
            this.iter = base.iterator();
            this.predicate = predicate;
        }

        @Override
        public boolean hasNext() {
            while (!testedFalse && iter.hasNext()) {
                current = iter.next();
                if (!(Boolean)predicate.invoke(context, current, index)) {
                    testedFalse = true;
                }
            }
            if (iter.hasNext()) {
                current = iter.next();
                return true;
            }
            return false;
        }
    }
}
