package com.sun.el.query;

import java.util.Iterator;
import javax.el.ELContext;
import javax.el.LambdaExpression;

import com.sun.el.lang.ELArithmetic;

class Average extends QueryOperator {

    @Override
    public Number invoke(final ELContext context,
                         final Iterable<Object> base,
                         final Object[] params) {
        final LambdaExpression selector = getLambda("sum", params, 0, true);
        Iterator<Object> iter = base.iterator();

        Number sum = Long.valueOf(0);
        long count = 0;
        while (iter.hasNext()) {
            Object item = iter.next();
            count++;
            if (selector != null) {
                item = selector.invoke(context, item);
            }
            sum = ELArithmetic.add(sum, item);
        }
        return ELArithmetic.divide(sum, count);
    }
}
