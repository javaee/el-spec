package com.sun.el.query;

import javax.el.ELContext;
import java.util.List;

class ElementAtOrDefault extends QueryOperator {

    @Override
    public Object invoke(final ELContext context,
                         final Iterable<Object> base,
                         final Object[] params) {
        final int index = getInt("elementAtOrDefault", params, 0);

        if (base instanceof List) {
            List<Object> list = (List<Object>) base;
            if (index < 0 || index >= list.size()) {
                return null;
            }
            return ((List<Object>)base).get(index);
        }

        if (index >=0 ) {
            int pindex = 0;
            for (Object item: base) {
                if (index == pindex++) {
                    return item;
                }
            }
        }
        return null;
    }
}
