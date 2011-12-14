package com.sun.el.query;

import java.util.Iterator;
import javax.el.ELContext;
import javax.el.LambdaExpression;

class Aggregate extends QueryOperator {
    @Override
    public Object invoke(final ELContext context,
                                   final Iterable<Object> base,
                                   final Object[] params) {
        int curArg = 0;
        // Get the seed and set it to the accumulator
        Object accumulator = getArgument("aggregate", params, curArg);
        LambdaExpression func;
        if (accumulator instanceof LambdaExpression) {
            func = (LambdaExpression) accumulator;
            accumulator = null;
        } else {
            curArg++;
            func = getLambda("aggregate", params, curArg++);
        }
        LambdaExpression resultSelector =
                getLambda("aggregate", params, curArg, true);
        Iterator<Object> iter = base.iterator();

        while (iter.hasNext()) {
            Object element = iter.next();
            if (accumulator == null) {
                accumulator = element;
            } else {
                accumulator = func.invoke(context, accumulator, element);
            }
        }
        if (resultSelector == null) {
            return accumulator;
        }
        return resultSelector.invoke(context, accumulator);
    }
}
