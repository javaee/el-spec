package com.sun.el.query;

import java.util.Iterator;
import javax.el.ELContext;
import javax.el.LambdaExpression;

class All extends QueryOperator {

    @Override
    public Object invoke(final ELContext context,
                         final Iterable<Object> base,
                         final Object[] params) {
        final LambdaExpression predicate = getLambda("all", params, 0);
        Iterator<Object> iter = base.iterator();

        while (iter.hasNext()) {
            if (!(Boolean)predicate.invoke(context, iter.next())) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE; 
    }
}
