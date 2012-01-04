package com.sun.el.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

import javax.el.ELContext;
import javax.el.LambdaExpression;

class OrderByDescending extends QueryOperator {
    @Override
    public Iterable<Object> invoke(final ELContext context,
                                   final Iterable<Object> base,
                                   final Object[] params) {
        final LambdaExpression keySelector =
                getLambda("orderByDescending", params, 0);
        final Comparator<Object> cmp =
                getComparator("orderByDescending", params, 1, true);

        // First make a copy of the Iterable
        List<Object> list = new ArrayList<Object>();
        for (Object item: base) {
            list.add(item);
        }

        return new OrderIterable(context, list, keySelector, cmp, true);
    }
}

