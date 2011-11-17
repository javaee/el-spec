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
        final LambdaExpression outer_selector = getLambda("join", params, 1);
        final LambdaExpression inner_selector = getLambda("join", params, 2);
        final LambdaExpression resultSelector = getLambda("join", params, 3);
        return new Iterable<Object>() {
            @Override
            public Iterator<Object> iterator() {
                return new JoinIterator(context, base, inner,
                            outer_selector, inner_selector, resultSelector);                }
        };
    }

    private static class JoinIterator extends BaseIterator {

        private ELContext context;
        private Iterable<Object> inner;
        private Iterator<Object> innerIter, outerIter;
        private LambdaExpression outerSelector, innerSelector, resultSelector;
        private Object currentOuter;
        private Object key;

        public JoinIterator(ELContext context,
                            Iterable<Object> base,
                            Iterable<Object> inner,
                            LambdaExpression outerSelector,
                            LambdaExpression innerSelector,
                            LambdaExpression resultSelector) {
            this.context = context;
            outerIter = base.iterator();
            this.inner = inner;
            this.outerSelector = outerSelector;
            this.innerSelector = innerSelector;
            this.resultSelector = resultSelector;
        }

        @Override
        public boolean hasNext() {
            if (innerIter != null) {
                while (innerIter.hasNext()) {
                    Object currentInner = innerIter.next();
                    if (key.equals(innerSelector.invoke(context,
                                                        currentInner))){
                        current = resultSelector.invoke(context, currentOuter,
                                                        currentInner);
                        return true;
                    }
                }
                innerIter = null;
                return hasNext();
            }
            if (outerIter.hasNext()) {
                currentOuter = outerIter.next();
                key = outerSelector.invoke(context, currentOuter);
                innerIter = inner.iterator();
                return hasNext();
            }
            return false;
        }
    }
}

