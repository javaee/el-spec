package com.sun.el.query;

import java.util.Iterator;
import javax.el.ELContext;
import javax.el.LambdaExpression;

class Where extends QueryOperator {
    @Override
    public Iterable<Object> invoke(final ELContext context,
                                   final Iterable<Object> base,
                                   final Object[] params) {
        final LambdaExpression predicate = getLambda("where", params, 0);
        return new Iterable<Object>() {
            @Override
            public Iterator<Object> iterator() {
                return new WhereIterator(context, base, predicate);
            }
        };
    }

    private static class WhereIterator extends BaseIterator {

        private ELContext context;
        private Iterator<Object> iter;
        private LambdaExpression predicate;

        public WhereIterator(ELContext context, Iterable<Object>base,
                             LambdaExpression predicate) {
            this.context = context;
            this.iter = base.iterator();
            this.predicate = predicate;
        }

        @Override
        public boolean hasNext() {
            while(iter.hasNext()) {
                current = iter.next();
                if ((Boolean)predicate.invoke(context, current)) {
                    return true;
                }
            }
            return false;
        }
    }
}
