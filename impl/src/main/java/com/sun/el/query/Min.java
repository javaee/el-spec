package com.sun.el.query;

import javax.el.ELContext;
import javax.el.LambdaExpression;

import com.sun.el.lang.ELSupport;

class Min extends QueryOperator {

    @Override
    public Object invoke(final ELContext context,
                         final Iterable<Object> base,
                         final Object[] params) {
        final LambdaExpression selector = getLambda("min", params, 0, true);

        Object min = null;
        for (Object item: base) {
            if (selector != null) {
                item = selector.invoke(context, item);
            }
            if (min == null || ELSupport.compare(min, item) > 0) {
                min = item;
            }
        }
        return min;
    }
}
