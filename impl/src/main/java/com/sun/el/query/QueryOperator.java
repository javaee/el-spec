package com.sun.el.query;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;

import javax.el.ELException;
import javax.el.ELContext;
import javax.el.LambdaExpression;

import com.sun.el.lang.ELArithmetic;

/**
 * Implementation of LINQ operators in EL.
 *
 * @author Kin-man Chung
 */

public abstract class QueryOperator {

    static HashMap<String, Class<? extends QueryOperator>> operators =
                new HashMap<String, Class<? extends QueryOperator>>();
    static {
        operators.put("where", Where.class);
        operators.put("select", Select.class);
        operators.put("selectMany", SelectMany.class);
        operators.put("sum", Sum.class);
//        operators.put("average", Average.class);
    }

    public abstract Object invoke(ELContext context,
                                  Iterable<Object> base, Object[] params);

    public static QueryOperator getQueryOperator(String operator) {
        Class<? extends QueryOperator> operatorClass = operators.get(operator);
        QueryOperator opr = null;
        if (operatorClass != null) {
            try {
                opr = operatorClass.newInstance();
            } catch (Exception ex) {
                return null;
            }
        }
        return opr;
    }

    private static LambdaExpression getLambda(Object[] params, int i) {
        if (i > params.length) {
            return null;
        }
        if (! (params[i] instanceof LambdaExpression)) {
            throw new ELException("Expecting a Lambda Expression argument");
        }
        return (LambdaExpression)params[i];
    }

// --------------------- Query Operators ------------------------

    static class Where extends QueryOperator {
        @Override
        public Iterable<Object> invoke(final ELContext context,
                                       final Iterable<Object> base,
                                       final Object[] params) {
            final LambdaExpression predicate = getLambda(params, 0);
            return new Iterable<Object>() {
                @Override
                public Iterator<Object> iterator() {
                    return new WhereIterator(context, base, predicate);
                }
            };
        }
    }

    static class Select extends QueryOperator {
        @Override
        public Iterable<Object> invoke(final ELContext context,
                                       final Iterable<Object> base,
                                       final Object[] params) {
            final LambdaExpression selector = getLambda(params, 0);
            return new Iterable<Object>() {
                @Override
                public Iterator<Object> iterator() {
                    return new SelectIterator(
                        context, base, selector);
                }
            };
        }
    }

    static class SelectMany extends QueryOperator {
        @Override
        public Iterable<Object> invoke(final ELContext context,
                                       final Iterable<Object> base,
                                       final Object[] params) {
            final LambdaExpression selector = getLambda(params, 0);
            final LambdaExpression resultSelector = getLambda(params, 1);
            return new Iterable<Object>() {
                @Override
                public Iterator<Object> iterator() {
                    return new SelectManyIterator(
                                   context, base, selector, resultSelector);
                }
            };
        }
    }

    static class Distinct extends QueryOperator {
        @Override
        public Iterable<Object> invoke(final ELContext context,
                                       final Iterable<Object> base,
                                       final Object[] params) {
            return new Iterable<Object>() {
                @Override
                public Iterator<Object> iterator() {
                    return new DistinctIterator(base);
                }
            };
        }
    }

    static class Sum extends QueryOperator {

        @Override
        public Number invoke(final ELContext context,
                             final Iterable<Object> base,
                             final Object[] params) {
            final LambdaExpression selector = getLambda(params, 0);
            Iterator<Object> iter =
                new SelectIterator(context, base, selector);

            Number sum = Long.valueOf(0);
            while (iter.hasNext()) {
                sum = ELArithmetic.add(sum, iter.next());
            }
            return sum;
        }
    }

/* XXX
    static class Average extends QueryOperator {
    
        @Override
        public Number invoke(final Iterable<Object> base,
                             final Object[] params) {
            Number sum = (Number) (new Sum().invoke(base, params));
            int count = (int) (new Count().invoke(base, params));
            return ELArithmetic.div(sum, count);
        }
    }
*/
            
// ----------------------- Iterators for Query Operators ----------------

    static abstract class BaseIterator implements Iterator<Object> {

        Object current;
        int index;

        @Override
        public Object next() {
            index++;
            return current;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
        
    static class WhereIterator extends BaseIterator {
 
        private ELContext context;
        private Iterator<Object> iter;
        private LambdaExpression predicate;

        public WhereIterator(ELContext context, Iterable<Object>base,
                             LambdaExpression predicate) {
            this.context = context;
            this.iter = base.iterator();
            this.predicate = predicate;
        }

        @Override
        public boolean hasNext() {
            while(iter.hasNext()) {
                current = iter.next();
                if ((Boolean)predicate.invoke(context, current)) {
                    return true;
                }
            }
            return false;
        }
    }

    static class SelectIterator extends BaseIterator {

        ELContext context;
        private Iterator<Object> iter;
        private LambdaExpression selector;

        public SelectIterator(ELContext context,
                              Iterable<Object>base, LambdaExpression selector) {
            this.context = context;
            this.iter = base.iterator();
            this.selector = selector;
        }

        @Override
        public boolean hasNext() {
            if (iter.hasNext()) {
                current = selector.invoke(context, iter.next(), index);
                return true;
            }
            return false;
        }
    }

    static class SelectManyIterator extends BaseIterator {

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
                        current = resultSelector.invoke(context, source_element,
                                                        current);
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

    static class DistinctIterator extends BaseIterator {

        private Iterator<Object> iter;
        private Set<Object> set;

        public DistinctIterator(Iterable<Object> base) {
            set = new HashSet<Object>();
            iter = base.iterator();
        }

        @Override
        public boolean hasNext() {
            while (iter.hasNext()) {
                current = iter.next();
                if (set.add(current)) {
                    return true;
                }
            }
            return false;
        }
    }
}

