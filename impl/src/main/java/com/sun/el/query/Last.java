package com.sun.el.query;

import javax.el.ELContext;
import javax.el.LambdaExpression;
import javax.el.InvalidOperationException;

import com.sun.el.lang.ELArithmetic;

class Last extends QueryOperator {

    @Override
    public Object invoke(final ELContext context,
                         final Iterable<Object> base,
                         final Object[] params) {
        final LambdaExpression predicate = getLambda("last", params, 0, true);

        Object last = null;
        boolean visited = false;
        for (Object item: base) {
            if (predicate == null || (Boolean)predicate.invoke(context, item)) {
                visited = true;
                last = item;
            }
        }
        if (! visited) {
            throw new InvalidOperationException();
        }
        return last;
    }
}
