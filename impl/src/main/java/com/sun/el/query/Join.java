package com.sun.el.query;

import java.util.Iterator;
import javax.el.ELContext;
import javax.el.LambdaExpression;

class Join extends QueryOperator {
    @Override
    public Iterable<Object> invoke(final ELContext context,
                                   final Iterable<Object> base,
                                   final Object[] params) {
        final Iterable<Object> inner = getIterable("join", params, 0);
        final LambdaExpression outerSelector = getLambda("join", params, 1);
        final LambdaExpression innerSelector = getLambda("join", params, 2);
        final LambdaExpression resultSelector = getLambda("join", params, 3);
        return new Iterable<Object>() {
            @Override
            public Iterator<Object> iterator() {
                return new BaseDoubleIterator(base) {
                    private Object key;

                    @Override
                    Iterator<Object> doItem(Object item) {
                        key = outerSelector.invoke(context, item);
                        return inner.iterator();
                    }

                    @Override
                    void doItem(Object item, Object item2) {
                        if (key.equals(innerSelector.invoke(context, item2))) {
                            yield(resultSelector.invoke(context, item, item2));
                        }
                    }
                };
            }
        };
    }
}

