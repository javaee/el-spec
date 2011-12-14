package com.sun.el.query;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Comparator;

import javax.el.ELException;
import javax.el.ELContext;
import javax.el.LambdaExpression;

/**
 * Implementation of LINQ operators in EL.
 *
 * @author Kin-man Chung
 */

public abstract class QueryOperator {

    static final HashMap<String, QueryOperator> operators =
                new HashMap<String, QueryOperator>();
    static {
        operators.put("where", new Where());
        operators.put("select", new Select());
        operators.put("selectMany", new SelectMany());
        operators.put("sum", new Sum());
//        operators.put("average", Average.class);
    }

    public abstract Object invoke(ELContext context,
                                  Iterable<Object> base, Object[] params);

    public static QueryOperator getQueryOperator(String operator) {
        return operators.get(operator);
    }

    static LambdaExpression getLambda(String name, Object[] params, int i) {
        return getLambda(name, params, i, false);
    }

    /*
     * Get an optional Lambda argument, which must be the last argument.
     * @return The Lambda expression if present, null otherwise
     */
    static LambdaExpression getLambda(String name, Object[] params, int i,
                                      boolean optional) {
        if (i > params.length || !(params[i] instanceof LambdaExpression)) {
            if (optional) {
                return null;
            }
            throw new ELException("Expecting a Lambda Expression for " + 
                "argument " + i + " of " + name + " operator.");
        }
        return (LambdaExpression)params[i];
    }

    static Object getArgument(String name, Object[] params, int i) {
        if (i > params.length ) {
            throw new ELException("Expecting argument " + i + " of " +
                name + " operator.");
        }
        return params[i];
    }

    static int getInt(String name, Object[] params, int i) {
        if (i > params.length || ! (params[i] instanceof Integer)) {
            throw new ELException("Expecting an integer for " + 
                "argument " + i + " of " + name + " operator.");
        }
        return (Integer) params[i];
    }

    static Iterable<Object> getIterable(String name, Object[] params, int i) {
        if (i > params.length || ! (params[i] instanceof Iterable)) {
            throw new ELException("Expecting an Iterable for " + 
                "argument " + i + " of " + name + " operator.");
        }
        @SuppressWarnings("unchecked")
        Iterable<Object> result = (Iterable<Object>) params[i];
        return result;
    }

    static Comparator getComparator(String name, Object[] params, int i,
                                    boolean optional) {
        if (i > params.length || ! (params[i] instanceof Comparator)) {
            if (optional) {
                return null;
            }
            throw new ELException("Expecting a Comparartor for " + 
                "argument " + i + " of " + name + " operator.");
        }
        return (Comparator) params[i];
    }

}

