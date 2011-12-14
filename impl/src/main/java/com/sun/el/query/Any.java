package com.sun.el.query;

import java.util.Iterator;
import javax.el.ELContext;
import javax.el.LambdaExpression;

class Any extends QueryOperator {

    @Override
    public Object invoke(final ELContext context,
                         final Iterable<Object> base,
                         final Object[] params) {
        final LambdaExpression predicate = getLambda("any", params, 0, true);
        Iterator<Object> iter = base.iterator();

        while (iter.hasNext()) {
            if (predicate == null) {
                return Boolean.TRUE;
            }
            if ((Boolean)predicate.invoke(context, iter.next())) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
}
