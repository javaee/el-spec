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
        operators.put("take", new Take());
        operators.put("skip", new Skip());
        operators.put("takeWhile", new TakeWhile());
        operators.put("skipWhile", new SkipWhile());
        operators.put("join", new Join());
        operators.put("groupJoin", new GroupJoin());
        operators.put("concat", new Concat());
        operators.put("orderBy", new OrderBy());
        operators.put("thenBy", new ThenBy());
        operators.put("orderByDescending", new OrderByDescending());
        operators.put("thenByDescending", new ThenByDescending());
        operators.put("reverse", new Reverse());
        operators.put("groupBy", new GroupBy());
        operators.put("distinct", new Distinct());
        operators.put("union", new Union());
        operators.put("intersect", new Intersect());
        operators.put("except", new Except());
        operators.put("toArray", new ToArray());
        operators.put("toList", new ToList());
        operators.put("toMap", new ToMap());
        operators.put("toLookup", new ToLookup());
        operators.put("sequenceEqual", new SequenceEqual());
        operators.put("first", new First());
        operators.put("firstOrDefault", new FirstOrDefault());
        operators.put("last", new Last());
        operators.put("lastOrDefault", new LastOrDefault());
        operators.put("single", new Single());
        operators.put("singleOrDefault", new SingleOrDefault());
        operators.put("elementAt", new ElementAt());
        operators.put("elementAtOrDefault", new ElementAtOrDefault());
        operators.put("defaultEmpty", new DefaultEmpty());
        operators.put("any", new Any());
        operators.put("all", new All());
        operators.put("contains", new Contains());
        operators.put("count", new Count());
        operators.put("sum", new Sum());
        operators.put("min", new Min());
        operators.put("max", new Max());
        operators.put("average", new Average());
        operators.put("aggregate", new Aggregate());
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
        if (i >= params.length || !(params[i] instanceof LambdaExpression)) {
            if (optional) {
                return null;
            }
            throw new ELException("Expecting a Lambda Expression for " + 
                "argument " + i + " of " + name + " operator.");
        }
        return (LambdaExpression)params[i];
    }

    static Object getArgument(String name, Object[] params, int i) {
        return getArgument(name, params, i, false);
    }

    static Object getArgument(String name, Object[] params, int i,
                              boolean optional) {
        if (i >= params.length ) {
            if (optional) {
                return null;
            }
            throw new ELException("Expecting argument " + i + " of " +
                name + " operator.");
        }
        return params[i];
    }

    static int getInt(String name, Object[] params, int i) {
        String msg = "Expecting an integer for " + 
            "argument " + i + " of " + name + " operator.";
        if (i >= params.length) {
            throw new ELException(msg);
        }
        return toInteger(params[i], msg);
    }

    static Iterable<Object> getIterable(String name, Object[] params, int i) {
        if (i >= params.length || ! (params[i] instanceof Iterable)) {
            throw new ELException("Expecting an Iterable for " + 
                "argument " + i + " of " + name + " operator.");
        }
        @SuppressWarnings("unchecked")
        Iterable<Object> result = (Iterable<Object>) params[i];
        return result;
    }

    static Comparator<Object> getComparator(String name, Object[] params, int i,
                                    boolean optional) {
        if (i >= params.length || ! (params[i] instanceof Comparator)) {
            if (optional) {
                return null;
            }
            throw new ELException("Expecting a Comparartor for " + 
                "argument " + i + " of " + name + " operator.");
        }
        @SuppressWarnings("unchecked")
        Comparator<Object> result = (Comparator<Object>)params[i];
        return result;
    }

    private static int toInteger(Object p, String msg) {
        if (p instanceof Integer) {
            return ((Integer) p).intValue();
        }
        if (p instanceof Character) {
            return ((Character) p).charValue();
        }
        if (p instanceof Number) {
            return ((Number) p).intValue();
        }
        if (p instanceof String) {
            return Integer.parseInt((String) p);
        }
        throw new IllegalArgumentException(msg);
    }
}

