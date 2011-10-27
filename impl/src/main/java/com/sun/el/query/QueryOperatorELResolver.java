package com.sun.el.query;

import java.util.Iterator;
import java.beans.FeatureDescriptor;

import javax.el.ELContext;
import javax.el.ELResolver;

/*
 * This ELResolver handles the operators (implemented with build-in
 * methods in Iterable's) in LINQ, .NET Language Integrated Query.  See
 * http://msdn.microsoft.com/en-us/library/bb394939.aspx
 */

public class QueryOperatorELResolver extends ELResolver {

    public Object invoke(ELContext context,
                         Object base,
                         Object method,
                         Class<?>[] paramTypes,
                         Object[] params) {

        if (context == null) {
            throw new NullPointerException();
        }

        if (! (base instanceof Iterable && method instanceof String)) {
            return null;
        }

        QueryOperator operator =
            QueryOperator.getQueryOperator(method.toString());
        if (operator == null) {
            return null;
        }

        context.setPropertyResolved(true);
        @SuppressWarnings("unchecked")
        Iterable<Object> iterable = (Iterable<Object>) base;
        return operator.invoke(context, iterable, params);
    }

    public Object getValue(ELContext context, Object base, Object property) {
        return null;
    }

    public Class<?> getType(ELContext context, Object base, Object property) {
        return null;
    }

    public void setValue(ELContext context, Object base, Object property,
                                  Object value) {
    }

    public boolean isReadOnly(ELContext context, Object base, Object property) {
        return false;
    }

    public Iterator<FeatureDescriptor> getFeatureDescriptors(
                                            ELContext context,
                                            Object base) {
        return null;
    }

    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        return String.class;
    }
}
