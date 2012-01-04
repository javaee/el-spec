package com.sun.el.query;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

import javax.el.LambdaExpression;
import javax.el.ELContext;

/*
 * This implements the operators orderedBy and thenBy, orderByDescending and
 * thenByDescending operators.
 *
 * To avoid unnecessary intemmediate sorting, the key selectors and the key
 * comparators are kept and applied only on the last operator, and only when
 * the iterator of the final Iterable is needed.
 *
 * A Comparator is used to handle the sorting of the primary and secondary
 * keys.
 */

class OrderIterable implements Iterable<Object> {

    final private List<OrderParam> orderParams =
                new ArrayList<OrderParam>();

    ELContext context;

    private List<Object> base;
    private boolean sorted = false;

    public OrderIterable(ELContext context, List<Object> base,
                         LambdaExpression keySelector, Comparator<Object> cmp,
                         boolean descending) {
        this.context = context;
        this.base = base;
        orderParams.add(new OrderParam(keySelector, cmp, descending));
    }

    private void sort() {
        if (sorted) {
            return;
        }
        Collections.sort(base, new ItemComparator());
        sorted = true;
    }
        
    void addOrder(LambdaExpression keySelector, Comparator<Object> cmp,
                  boolean descending) {
        orderParams.add(new OrderParam(keySelector, cmp, descending));
    }

    @Override
    public Iterator<Object> iterator() {
        sort();
        return base.iterator();
    }

    private class ItemComparator implements Comparator<Object> {

        @Override
        public int compare(Object item, Object item2) {
            for (OrderParam orderParam: orderParams) {
                Object key1 = orderParam.getSelector().invoke(context, item);
                Object key2 = orderParam.getSelector().invoke(context, item2);
                Comparator<Object> comparator = orderParam.getComparator();
                int result = 0;
                if (comparator != null) {
                    result = comparator.compare(key1, key2);
                } else if (key1 instanceof Comparable) {
                    @SuppressWarnings("unchecked")
                    int temp = ((Comparable<Object>)key1).compareTo(key2);
                    result = temp;
                }
                if (result != 0) {
                    if (orderParam.isDescending()) {
                        return -result;
                    }
                    return result;
                }
            }
            return 0;
        }
    }

    private class OrderParam {
        private LambdaExpression selector;
        private Comparator<Object> comparator;
        private boolean descending;

        OrderParam(LambdaExpression selector,
                   Comparator<Object> comparator,
                   boolean descending) {
            this.selector = selector;
            this.comparator = comparator;
            this.descending = descending;
        }

        LambdaExpression getSelector() {
            return this.selector;
        }

        Comparator<Object> getComparator() {
            return this.comparator;
        }

        boolean isDescending() {
            return this.descending;
        }
    }
}


