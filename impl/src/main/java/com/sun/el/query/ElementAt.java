package com.sun.el.query;

import javax.el.ELContext;
import java.util.List;

class ElementAt extends QueryOperator {

    @Override
    public Object invoke(final ELContext context,
                         final Iterable<Object> base,
                         final Object[] params) {
        final int index = getInt("elementAt", params, 0);

        if (base instanceof List) {
            return ((List<Object>)base).get(index);
        }

        int pindex = 0;
        if (index >=0 ) {
            for (Object item: base) {
                if (index == pindex++) {
                    return item;
                }
            }
        }
        throw new IndexOutOfBoundsException();
    }
}
