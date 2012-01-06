package com.sun.el.query;

import javax.el.ELContext;
import javax.el.LambdaExpression;

import com.sun.el.lang.ELSupport;

class Max extends QueryOperator {

    @Override
    public Object invoke(final ELContext context,
                         final Iterable<Object> base,
                         final Object[] params) {
        final LambdaExpression selector = getLambda("max", params, 0, true);

        Object max = null;
        for (Object item: base) {
            if (selector != null) {
                item = selector.invoke(context, item);
            }
            if (max == null || ELSupport.compare(max, item) < 0) {
                max = item;
            }
        }
        return max;
    }
}
