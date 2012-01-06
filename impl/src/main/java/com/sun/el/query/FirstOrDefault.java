package com.sun.el.query;

import javax.el.ELContext;
import javax.el.LambdaExpression;

class FirstOrDefault extends QueryOperator {

    @Override
    public Object invoke(final ELContext context,
                         final Iterable<Object> base,
                         final Object[] params) {
        final LambdaExpression predicate = getLambda("firstOrDefault",
                                                     params, 0, true);

        for (Object item: base) {
            if (predicate == null || (Boolean)predicate.invoke(context, item)) {
                return item;
            }
        }
        return null;
    }
}
