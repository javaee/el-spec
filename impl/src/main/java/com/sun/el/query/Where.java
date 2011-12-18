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
                return new BaseIterator(base) {
                    @Override
                    public void doItem(Object item) {
                        if ((Boolean) predicate.invoke(context, item, index)) {
                            yield(item);
                        }
                    }
                };
            }
        };
    }
}
