package com.sun.el.query;

import javax.el.ELContext;
import javax.el.LambdaExpression;

class All extends QueryOperator {

    @Override
    public Object invoke(final ELContext context,
                         final Iterable<Object> base,
                         final Object[] params) {
        final LambdaExpression predicate = getLambda("all", params, 0);

        for (Object item: base) {
            if (!(Boolean)predicate.invoke(context, item)) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE; 
    }
}
