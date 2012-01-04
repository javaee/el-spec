package com.sun.el.query;

import java.util.Iterator;
import java.util.Comparator;

import javax.el.ELContext;
import javax.el.LambdaExpression;

class SequenceEqual extends QueryOperator {

    @Override
    public Boolean invoke(final ELContext context,
                         final Iterable<Object> base,
                         final Object[] params) {

        final Iterable<Object> second = getIterable("sequenceEqual", params, 0);
        final Comparator cmp = getComparator("sequenceEqual", params, 1, true);

        Iterator<Object> iter = base.iterator();
        Iterator<Object> iter2 = second.iterator();

        while (iter.hasNext() && iter2.hasNext()) {
            if (iter.next() != iter2.next()) {
                return false;
            }
        }
        return (!iter.hasNext() && !iter2.hasNext());
    }
}
