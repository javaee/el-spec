package com.sun.el.query;

import java.util.Iterator;
import java.util.ArrayList;
import javax.el.ELContext;

class Reverse extends QueryOperator {
    @Override
    public Iterable<Object> invoke(final ELContext context,
                                   final Iterable<Object> base,
                                   final Object[] params) {

        return new Iterable<Object>() {
            @Override
            public Iterator<Object> iterator() {
                return new Iterator<Object>() {
                    private Object current;
                    private boolean yielded;
                    private ArrayList<Object> list = new ArrayList<Object>();
                    private int index = 0;

                    Iterator<Object> iter = base.iterator();
                    {   
                        while (iter.hasNext()) {
                            list.add(iter.next());
                            index++;
                        }
                    }

                    @Override
                    public Object next() {
                        yielded = false;
                        return current;
                    }

                    @Override
                    public boolean hasNext() {
                        if (!yielded && index > 0) {
                            yielded = true;
                            current = list.get(--index);
                        }
                        return yielded;
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }
}

