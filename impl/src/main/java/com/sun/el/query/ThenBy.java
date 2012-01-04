package com.sun.el.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

import javax.el.ELContext;
import javax.el.LambdaExpression;

class ThenBy extends QueryOperator {
    @Override
    public Iterable<Object> invoke(final ELContext context,
                                   final Iterable<Object> base,
                                   final Object[] params) {
        final LambdaExpression keySelector = getLambda("thenBy", params, 0);
        final Comparator<Object> cmp = getComparator("thenBy", params, 1, true);

        if (!(base instanceof OrderIterable)) {
            return base;
        }

        ((OrderIterable) base).addOrder(keySelector, cmp, false);
        return base;
    }
}

