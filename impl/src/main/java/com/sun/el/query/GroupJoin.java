package com.sun.el.query;

import java.util.Iterator;
import java.util.ArrayList;
import javax.el.ELContext;
import javax.el.LambdaExpression;

class GroupJoin extends QueryOperator {
    @Override
    public Iterable<Object> invoke(final ELContext context,
                                   final Iterable<Object> base,
                                   final Object[] params) {
        final Iterable<Object> inner = getIterable("groupJoin", params, 0);
        final LambdaExpression outerSelector = getLambda("groupJoin", params, 1);
        final LambdaExpression innerSelector = getLambda("groupJoin", params, 2);
        final LambdaExpression resultSelector = getLambda("groupJoin", params, 3);
        return new Iterable<Object>() {
            @Override
            public Iterator<Object> iterator() {
                return new BaseIterator(base) {
                    @Override
                    void doItem(Object item) {
                        Object key = outerSelector.invoke(context, item);
                        if (key == null) {
                            return;
                        }
                        ArrayList<Object> alist = new ArrayList<Object>();
                        for (Object innerItem: inner) {
                            if (key.equals(innerSelector.invoke(context,
                                                                innerItem))){
                                alist.add(innerItem);
                            }
                        }
                        if (alist.size() > 0) {
                            yield(resultSelector.invoke(context, item, alist));
                        }
                    }
                };
            }
        };
    }
}

