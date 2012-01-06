package com.sun.el.query;

import javax.el.ELContext;
import javax.el.LambdaExpression;

class LastOrDefault extends QueryOperator {

    @Override
    public Object invoke(final ELContext context,
                         final Iterable<Object> base,
                         final Object[] params) {
        final LambdaExpression predicate = getLambda("lastOrDefault",
                                                     params, 0, true);

        Object last = null;
        for (Object item: base) {
            if (predicate == null || (Boolean)predicate.invoke(context, item)) {
                last = item;
            }
        }
        return last;
    }
}
