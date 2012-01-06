package com.sun.el.query;

import javax.el.ELContext;
import javax.el.LambdaExpression;
import javax.el.InvalidOperationException;

class SingleOrDefault extends QueryOperator {

    @Override
    public Object invoke(final ELContext context,
                         final Iterable<Object> base,
                         final Object[] params) {
        final LambdaExpression predicate = getLambda("singleOrDefault",
                                                     params, 0, true);

        Object result = null;
        int count = 0;
        for (Object item: base) {
            if (predicate == null || (Boolean)predicate.invoke(context, item)) {
                count++;
                result = item;
            }
        }
        if (count > 1) {
            throw new InvalidOperationException();
        }
        return result;
    }
}
