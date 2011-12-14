package com.sun.el.query;

import java.util.Iterator;
import javax.el.ELContext;
import javax.el.LambdaExpression;

class Count extends QueryOperator {

    @Override
    public Number invoke(final ELContext context,
                         final Iterable<Object> base,
                         final Object[] params) {
        final LambdaExpression predicate = getLambda("count", params, 0, true);
        Iterator<Object> iter = base.iterator();

        long count = 0;
        if (predicate == null) {
            while (iter.hasNext()) {
                iter.next();
                count++;
            }
        } else {
            while (iter.hasNext()) {
                if ((Boolean)predicate.invoke(context, iter.next())) {
                    count++;
                }
            }
        }
        return Long.valueOf(count);
    }
}
