package com.sun.el.query;

import java.util.Iterator;
import javax.el.ELContext;
import javax.el.LambdaExpression;

class Select extends QueryOperator {
    @Override
    public Iterable<Object> invoke(final ELContext context,
                                   final Iterable<Object> base,
                                   final Object[] params) {
        final LambdaExpression selector = getLambda("select", params, 0);
        return new Iterable<Object>() {
            @Override
            public Iterator<Object> iterator() {
                return new BaseIterator(base) {
                    @Override
                    public void doItem(Object item) {
                        yield(selector.invoke(context, item, index));
                    }
                };
            }
        };
    }
}
