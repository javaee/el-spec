package com.sun.el.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

import javax.el.ELContext;
import javax.el.LambdaExpression;

class ThenByDescending extends QueryOperator {
    @Override
    public Iterable<Object> invoke(final ELContext context,
                                   final Iterable<Object> base,
                                   final Object[] params) {
        final LambdaExpression keySelector = getLambda("thenByDescending", params, 0);
        final Comparator<Object> cmp = getComparator("thenByDescending", params, 1, true);

        if (!(base instanceof OrderIterable)) {
            return base;
        }

        ((OrderIterable) base).addOrder(keySelector, cmp, true);
        return base;
    }
}

