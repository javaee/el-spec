package com.sun.el.query;

import java.util.Iterator;
import java.util.HashMap;

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

    // TODO: optional lambda
    static LambdaExpression getLambda(String name, Object[] params, int i) {
        if (i > params.length || !(params[i] instanceof LambdaExpression)) {
            throw new ELException("Expecting a Lambda Expression for " + 
                "argument " + i + " of " + name + " operator.");
        }
        return (LambdaExpression)params[i];
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
}

