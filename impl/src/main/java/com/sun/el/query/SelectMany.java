package com.sun.el.query;

import java.util.Iterator;
import javax.el.ELContext;
import javax.el.LambdaExpression;

class SelectMany extends QueryOperator {
    @Override
    public Iterable<Object> invoke(final ELContext context,
                                   final Iterable<Object> base,
                                   final Object[] params) {
        final LambdaExpression selector = getLambda("selectMany", params, 0);
        final LambdaExpression resultSelector =
                  getLambda("selectMany", params, 1, true);
        return new Iterable<Object>() {
            @Override
            public Iterator<Object> iterator() {
                return new BaseDoubleIterator(base) {
                    @Override
                    Iterator<Object> doItem(Object item) {
                        Object tmp = selector.invoke(context, item, index);
                        if (tmp instanceof Iterable) {
                            @SuppressWarnings("unchecked")
                            Iterator<Object> _iter = ((Iterable<Object>)tmp).iterator();
                            return _iter;
                        }
                        // Undefined.  We yield tmp, to continue
                        yield(tmp);
                        return null;
                    }

                    @Override
                    void doItem(Object item, Object innerItem){
                        if (resultSelector == null) {
                            yield(innerItem);
                        } else {
                            yield(resultSelector.invoke(context, item, innerItem));
                        }
                    }
                };
            }
        };
    }
}

