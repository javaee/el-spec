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
                return new SelectManyIterator(
                               context, base, selector, resultSelector);
            }
        };
    }

    private static class SelectManyIterator extends BaseIterator {

        ELContext context;
        private Iterator<Object> iter, inner_iter, outer_iter;
        private LambdaExpression selector, resultSelector;

        public SelectManyIterator(ELContext context,
                                  Iterable<Object> base,
                                  LambdaExpression selector,
                                  LambdaExpression resultSelector) {
            this.context = context;
            iter = outer_iter = base.iterator();
            this.selector = selector;
            this.resultSelector = resultSelector;
        }

        @Override
        public boolean hasNext() {
            Object source_element = null;
            if (iter.hasNext()) {
                if (iter == outer_iter) {
                    source_element = iter.next();
                    current = selector.invoke(context, source_element, index);
                    if (current instanceof Iterator) {
                        @SuppressWarnings("unchecked")
                        Iterator<Object> _iter = (Iterator<Object>) current;
                        iter = inner_iter = _iter;
                        return hasNext();
                    }
                } else {
                    current = iter.next();
                    if (resultSelector != null) {
                        current = resultSelector.invoke(context, source_element,                                                        current);
                    }
                }
                return true;
            } else {
                if (iter == inner_iter) {
                    iter = outer_iter;
                    return hasNext();
                }
                return false;
            }
        }
    }
}

