package com.sun.el.query;

import java.util.Iterator;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

import javax.el.ELContext;

class ToArray extends QueryOperator {
    @Override
    public Object[] invoke(final ELContext context,
                                   final Iterable<Object> base,
                                   final Object[] params) {

        if (base instanceof Collection) {
            return ((Collection<Object>) base).toArray();
        }

        List<Object> list = new ArrayList<Object>();
        for (Object obj: base) {
            list.add(obj);
        }
        return list.toArray();
    }
}
