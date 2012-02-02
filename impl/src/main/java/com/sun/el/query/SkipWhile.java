package com.sun.el.query;

import java.util.Iterator;
import javax.el.ELContext;
import javax.el.LambdaExpression;

class SkipWhile extends QueryOperator {
    @Override
    public Iterable<Object> invoke(final ELContext context,
                                   final Iterable<Object> base,
                                   final Object[] params) {
        final LambdaExpression predicate = getLambda("skipWhile", params, 0);
        return new Iterable<Object>() {
            @Override
            public Iterator<Object> iterator() {
                return new BaseIterator(base) {
                    boolean testedFalse = false;
                    @Override
                    void doItem(Object item) {
                        if (testedFalse) {
                            yield(item);
                            return;
                        }
                        if ((Boolean)predicate.invoke(context, item, index)) {
                            return;
                        }
                        testedFalse = true;
                        yield(item);
                    }
                };
            }
        };
    }
}
