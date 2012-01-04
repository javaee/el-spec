package com.sun.el.query;

import java.util.HashMap;
import java.util.Map;
import java.util.Comparator;

import javax.el.ELContext;
import javax.el.LambdaExpression;

class ToMap extends QueryOperator {
    @Override
    public Map<Object,Object> invoke(final ELContext context,
                                     final Iterable<Object> base,
                                     final Object[] params) {
        final LambdaExpression keySelector = getLambda("ToMap", params, 0);
        final LambdaExpression elementSelector =
                  getLambda("ToMap", params, 1, true);
        int indexC = 1;
        if (elementSelector != null) {
            indexC++;
        }
        final Comparator cmp = getComparator("ToMap", params, indexC, true);

        Map<Object, Object> map = new HashMap<Object, Object>();

        for (Object element: base) {
            Object key = keySelector.invoke(context, element);
            Object value = element;
            if (elementSelector != null) {
                value = elementSelector.invoke(context, element);
            }
            map.put(key, value);
        }
        return map;
    }
}

