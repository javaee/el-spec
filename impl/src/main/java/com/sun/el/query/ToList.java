package com.sun.el.query;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import javax.el.ELContext;

class ToList extends QueryOperator {
    @Override
    public Iterable<Object> invoke(final ELContext context,
                                   final Iterable<Object> base,
                                   final Object[] params) {

        if (base instanceof List) {
            return base;
        }

        List<Object> list = new ArrayList<Object>();
        for (Object obj: base) {
            list.add(obj);
        }
        return list;
    }
}
