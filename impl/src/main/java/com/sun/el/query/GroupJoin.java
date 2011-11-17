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
        final LambdaExpression outer_selector = getLambda("groupJoin", params, 1);
        final LambdaExpression inner_selector = getLambda("groupJoin", params, 2);
        final LambdaExpression resultSelector = getLambda("groupJoin", params, 3);
        return new Iterable<Object>() {
            @Override
            public Iterator<Object> iterator() {
                return new GroupJoinIterator(context, base, inner,
                            outer_selector, inner_selector, resultSelector);                }
        };
    }

    private static class GroupJoinIterator extends BaseIterator {

        ELContext context;
        Iterable<Object> inner;
        private Iterator<Object> innerIter, outerIter;
        private LambdaExpression outerSelector, innerSelector, resultSelector;

        public GroupJoinIterator(ELContext context,
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
            while (outerIter.hasNext()) {
                Object currentOuter = outerIter.next();
                Object key = outerSelector.invoke(context, currentOuter);
                innerIter = inner.iterator();
                ArrayList<Object> alist = new ArrayList<Object>();
                while (innerIter.hasNext()) {
                    Object currentInner = innerIter.next();
                    if (key.equals(innerSelector.invoke(context,
                                                        currentInner))){
                        alist.add(currentInner);
                    }
                }
                if (alist.size() > 0) {
                    current = resultSelector.invoke(context, currentOuter,
                                                    alist);
                    return true;
                }
            }
            return false;
        }
    }
}

