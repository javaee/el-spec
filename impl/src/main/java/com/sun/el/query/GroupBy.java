package com.sun.el.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

import javax.el.ELContext;
import javax.el.LambdaExpression;
import javax.el.Grouping;

class GroupBy extends QueryOperator {
    @Override
    public Iterable<ArrayListGrouping<Object,Object>> invoke(
                                   final ELContext context,
                                   final Iterable<Object> base,
                                   final Object[] params) {
        final LambdaExpression keySelector = getLambda("groupBy", params, 0);
        final LambdaExpression elementSelector =
                  getLambda("groupBy", params, 1, true);
        int indexC = 1;
        if (elementSelector != null) {
            indexC++;
        }
        final Comparator cmp = getComparator("groupBy", params, indexC, true);

        List<ArrayListGrouping<Object,Object>> groups =
                    new ArrayList<ArrayListGrouping<Object,Object>>();

        for (Object element: base) {
            Object key = keySelector.invoke(context, element);
            Object dest = key;
            if (elementSelector != null) {
                dest = elementSelector.invoke(context, element);
            }
            addToGroup(groups, key, dest);
        }
        return groups;
    }

    private void addToGroup(List<ArrayListGrouping<Object,Object>> groups,
                            Object key, Object value) {
        if (key == null || value == null) {
            return;
        }
        for (ArrayListGrouping<Object, Object> group: groups) {
            if (key.equals(group.getKey())) {
                group.add(value);
                return;
            }
        }
        ArrayListGrouping<Object, Object> g =
            new ArrayListGrouping<Object,Object>(key);
        g.add(value);
        groups.add(g);
        return;
    }
}

