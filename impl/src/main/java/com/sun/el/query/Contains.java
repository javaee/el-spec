package com.sun.el.query;

import java.util.Collection;
import javax.el.ELContext;

class Contains extends QueryOperator {

    @Override
    public Object invoke(final ELContext context,
                         final Iterable<Object> base,
                         final Object[] params) {
        Object element = getArgument("contains", params, 0);

        if (base instanceof Collection) {
            return ((Collection) base).contains(element);
        }

        for (Object item: base) {
            if (element.equals(item)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE; 
    }
}
